package com.nttai.webflux.utils;


import org.apache.logging.log4j.Logger;

public class LogUtils {
    private LogUtils() {}

    public static void logErrorWithRequestId(Logger logger, String requestId, String message, Throwable throwable) {
        if (logger != null) {
            logger.error("[{}] {}", requestId, message, throwable);
        }
    }

    public static void logInfoWithRequestId(Logger logger, String requestId, String message) {
        if (logger != null) {
            logger.info("[{}] {}", requestId, message);
        }
    }

    public static void logWarnWithRequestId(Logger logger, String requestId, String message, Throwable throwable) {
        if (logger != null) {
            logger.warn("[{}] {}", requestId, message, throwable);
        }
    }
} 