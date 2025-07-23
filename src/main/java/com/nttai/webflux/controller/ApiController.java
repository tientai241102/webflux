package com.nttai.webflux.controller;


import com.nttai.webflux.dto.BaseResponse;
import com.nttai.webflux.third_party.dto.PostResponse;
import com.nttai.webflux.service.ApiService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import com.nttai.webflux.utils.ResponseUtils;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;

@Tag(name = "API", description = "API endpoints")
@RestController
@RequestMapping("/api")
@Log4j2
public class ApiController {


    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @Operation(summary = "Get data", description = "Fetches data from third-party APIs and returns a standardized response.")
    @GetMapping("/data")
    public Mono<ResponseEntity<BaseResponse<List<PostResponse>>>> getData() {
        return apiService.processGetData()
            .map(data -> ResponseEntity.ok(ResponseUtils.success(data)));
    }
}