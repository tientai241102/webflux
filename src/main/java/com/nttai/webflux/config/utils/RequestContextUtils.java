package com.nttai.webflux.config.utils;

import reactor.core.publisher.Mono;

import java.util.Objects;

public class RequestContextUtils {

    private static final String REQUEST_ID_KEY = "requestId";

    private RequestContextUtils() {
        throw new IllegalStateException("Utility class");
    }
    public static Mono<String> getRequestId() {
        return Mono.deferContextual(context ->
                Mono.just(Objects.requireNonNull(context.getOrDefault(REQUEST_ID_KEY, "unknown")))
        );
    }
}