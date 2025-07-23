package com.nttai.webflux.config.service;


import com.nttai.webflux.config.constant.ErrorEnum;
import com.nttai.webflux.config.exception.BusinessException;
import com.nttai.webflux.config.third_party.ThirdPartyService;
import com.nttai.webflux.config.third_party.dto.PostResponse;
import com.nttai.webflux.config.utils.LogUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Log4j2

public class ApiService {

    private final ThirdPartyService thirdPartyService;
    private final int concurrency;

    public ApiService(ThirdPartyService thirdPartyService, @Value("${service.concurrency:10}") int concurrency) {
        this.thirdPartyService = thirdPartyService;
        this.concurrency = concurrency;
    }

    public Mono<List<PostResponse>> processGetData() {
        return Mono.deferContextual(context -> {
            String requestId = context.getOrDefault("requestId", "unknown");
            return thirdPartyService.fetchDataGetPost()
                    .flatMapMany(postsArray -> {
                        if (postsArray.length == 0) {
                            return Flux.error(new BusinessException(ErrorEnum.NO_POST));
                        }
                        return Flux.fromArray(postsArray)
                                .map(PostResponse::getId);
                    })
                    .flatMap(id -> thirdPartyService.fetchDataGetDetailPost(id)
                                    .doOnError(e -> LogUtils.logWarnWithRequestId(log, requestId, "Retry failed for id " + id + ": " + e.getMessage(), e)),
                            concurrency // now configurable
                    )
                    .collectList()
                    .doOnSuccess(result -> LogUtils.logInfoWithRequestId(log, requestId, "Completed API sequence"))
                    .doOnError(error -> LogUtils.logErrorWithRequestId(log, requestId, "Error during API call: " + error.getMessage(), error));
        });
    }

}