package com.nttai.webflux.config.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttai.webflux.config.constant.CommonConstant;
import com.nttai.webflux.config.constant.ErrorEnum;
import com.nttai.webflux.config.constant.HeaderEnum;
import com.nttai.webflux.config.exception.BusinessException;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@Log4j2
public class LogFilterConfig {


    @Bean
    public WebFilter logFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String uri = request.getURI().getPath();
            final String requestId = request.getHeaders().getFirst(HeaderEnum.X_REQUEST_ID.getValue()) != null
                    && !request.getHeaders().getFirst(HeaderEnum.X_REQUEST_ID.getValue()).isEmpty()
                    ? request.getHeaders().getFirst(HeaderEnum.X_REQUEST_ID.getValue())
                    : UUID.randomUUID().toString();

            // Bypass health check URLs
            if (uri.contains(CommonConstant.HEALTH_CHECK_URL)) {
                log.info("[{}] Bypassing health check: {}", requestId, uri);
                return chain.filter(exchange)
                        .contextWrite(context -> context.put(HeaderEnum.X_REQUEST_ID.getThreadId(), requestId));
            }

            // Log request details
            final long startTime = System.currentTimeMillis();
            log.info("[{}] Request - {} {} - Headers: {}",
                    requestId, request.getMethod(), uri, request.getHeaders());

            // Handle GET requests
            if (request.getMethod() == HttpMethod.GET) {
                return chain.filter(exchange)
                        .contextWrite(context -> context.put(HeaderEnum.X_REQUEST_ID.getThreadId(), requestId))
                        .doOnSuccess(v -> logResponse(exchange, requestId, startTime))
                        .doOnError(e -> log.error("[{}] Error - {}: {}", requestId, uri, e.getMessage()));
            }

            // Handle non-GET requests with body modification
            return DataBufferUtils.join(request.getBody())
                    .flatMap(dataBuffer -> {
                        try {
                            // Read request body as string
                            String bodyString = dataBuffer.toString(StandardCharsets.UTF_8);
                            DataBufferUtils.release(dataBuffer); // Release buffer to prevent memory leaks
                            String jsonBody = bodyString.isEmpty() ? "{}" : bodyString;

                            // Parse and modify JSON
                            ObjectMapper mapper = new ObjectMapper();
                            Map<String, Object> dataRequest = mapper.readValue(jsonBody, LinkedHashMap.class);
                            dataRequest.putIfAbsent("request_id", requestId);
                            dataRequest.put("lang", request.getHeaders().getFirst(HeaderEnum.X_LANG.getValue()));

                            // Create modified body
                            String modifiedBody = mapper.writeValueAsString(dataRequest);
                            DataBuffer modifiedBuffer = exchange.getResponse().bufferFactory()
                                    .wrap(modifiedBody.getBytes(StandardCharsets.UTF_8));

                            // Create decorated request with modified body
                            ServerHttpRequest modifiedRequest = new ServerHttpRequestDecorator(request) {
                                @Override
                                public Flux<DataBuffer> getBody() {
                                    return Flux.just(modifiedBuffer);
                                }
                            }.mutate()
                                    .header(HeaderEnum.X_REQUEST_ID.getValue(), requestId)
                                    .build();

                            ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();

                            return chain.filter(modifiedExchange)
                                    .contextWrite(context -> context.put(HeaderEnum.X_REQUEST_ID.getThreadId(), requestId))
                                    .doOnSuccess(v -> logResponse(modifiedExchange, requestId, startTime))
                                    .doOnError(e -> log.error("[{}] Error - {}: {}", requestId, uri, e.getMessage()));
                        } catch (Exception e) {
                            log.error("[{}] Error processing request body: {}", requestId, e.getMessage());
                            return Mono.error(new BusinessException(ErrorEnum.INTERNAL_SERVER_ERROR));
                        }
                    })
                    .switchIfEmpty(Mono.defer(() -> {
                        try {
                            // Handle empty body case
                            ObjectMapper mapper = new ObjectMapper();
                            Map<String, Object> dataRequest = new LinkedHashMap<>();
                            dataRequest.put("request_id", requestId);
                            dataRequest.put("lang", request.getHeaders().getFirst(HeaderEnum.X_LANG.getValue()));

                            // Create modified body
                            String modifiedBody = mapper.writeValueAsString(dataRequest);
                            DataBuffer modifiedBuffer = exchange.getResponse().bufferFactory()
                                    .wrap(modifiedBody.getBytes(StandardCharsets.UTF_8));

                            // Create decorated request with modified body
                            ServerHttpRequest modifiedRequest = new ServerHttpRequestDecorator(request) {
                                @Override
                                public Flux<DataBuffer> getBody() {
                                    return Flux.just(modifiedBuffer);
                                }
                            }.mutate()
                                    .header(HeaderEnum.X_REQUEST_ID.getValue(), requestId)
                                    .build();

                            ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();

                            return chain.filter(modifiedExchange)
                                    .contextWrite(context -> context.put(HeaderEnum.X_REQUEST_ID.getThreadId(), requestId))
                                    .doOnSuccess(v -> logResponse(modifiedExchange, requestId, startTime))
                                    .doOnError(e -> log.error("[{}] Error - {}: {}", requestId, uri, e.getMessage()));
                        } catch (Exception e) {
                            log.error("[{}] Error processing empty request body: {}", requestId, e.getMessage());
                            return Mono.error(new BusinessException(ErrorEnum.INTERNAL_SERVER_ERROR));
                        }
                    }));
        };
    }

    private void logResponse(ServerWebExchange exchange, String requestId, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        log.info("[{}] Response - {} {} - Status: {} - Duration: {}ms - Headers: {}",
                requestId,
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI(),
                exchange.getResponse().getStatusCode(),
                duration,
                exchange.getResponse().getHeaders());
    }
}