package com.nttai.webflux.config.third_party;

import com.nttai.webflux.config.config.ApiConfig;
import com.nttai.webflux.config.constant.ErrorEnum;
import com.nttai.webflux.config.exception.BusinessException;
import com.nttai.webflux.config.utils.LogUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Log4j2
public abstract class BaseApiService {

    @Autowired
    @Qualifier("thirdPartyWebClients")
    protected Map<String, WebClient> thirdPartyWebClients;

    @Autowired
    @Qualifier("thirdPartyApiConfigs")
    protected Map<String, ApiConfig> thirdPartyApiConfigs;

    public <T, R> Mono<R> callApi(String baseUrl, String endpoint,
                                  WebClient webClient, HttpMethod method,
                                  HttpHeaders headers, T requestBody,
                                  Class<R> responseType) {

        String fullUrl = baseUrl + endpoint;

        return Mono.deferContextual(context -> {
            String requestId = context.getOrDefault("requestId", "unknown");

            return webClient.method(method)
                    .uri(endpoint)
                    .headers(httpHeaders -> {
                        if (headers != null) httpHeaders.addAll(headers);
                        if (requestBody != null)
                            httpHeaders.set(HttpHeaders.CONTENT_TYPE, "application/json");
                    })
                    .body(requestBody != null ? Mono.just(requestBody) : Mono.empty(), Object.class)
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> response.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                LogUtils.logWarnWithRequestId(log, requestId, "API error (" + response.statusCode() + "): " + errorBody, null);
                                return Mono.error(new BusinessException(ErrorEnum.THIRD_PARTY_ERROR));
                            }))
                    .bodyToMono(responseType)
                    .doOnSubscribe(sub -> LogUtils.logInfoWithRequestId(log, requestId, "HTTP " + method + " -> " + fullUrl))
                    .doOnSuccess(res -> LogUtils.logInfoWithRequestId(log, requestId, "Success " + method + " -> " + fullUrl))
                    .doOnError(err -> LogUtils.logErrorWithRequestId(log, requestId, "Failed " + method + " -> " + fullUrl + ": " + err.getMessage(), err));
        });
    }
}