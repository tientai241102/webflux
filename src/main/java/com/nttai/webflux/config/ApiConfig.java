package com.nttai.webflux.config;

import lombok.Data;

@Data
public class ApiConfig {
    private String url;
    private int timeout;
    private int maxConnections;
    private int retryMaxAttempts;
    private int retryDelayMs;
}
