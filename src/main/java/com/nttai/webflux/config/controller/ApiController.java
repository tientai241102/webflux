package com.nttai.webflux.config.controller;


import com.nttai.webflux.config.dto.BaseResponse;
import com.nttai.webflux.config.service.ApiService;
import com.nttai.webflux.config.third_party.dto.PostResponse;
import com.nttai.webflux.config.utils.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

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