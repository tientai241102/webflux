package com.nttai.webflux.utils;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


class LogUtilsTest {
    @Test
    void testLogErrorWithRequestId() {
        Logger logger = Mockito.mock(Logger.class);
        LogUtils.logErrorWithRequestId(logger, "req-123", "Error message", new RuntimeException("fail"));
        Mockito.verify(logger).error(Mockito.eq("[{}] {}"), Mockito.eq("req-123"), Mockito.eq("Error message"), Mockito.any(Throwable.class));
    }

    @Test
    void testLogInfoWithRequestId() {
        Logger logger = Mockito.mock(Logger.class);
        LogUtils.logInfoWithRequestId(logger, "req-456", "Info message");
        Mockito.verify(logger).info(Mockito.eq("[{}] {}"), Mockito.eq("req-456"), Mockito.eq("Info message"));
    }

    @Test
    void testLogWarnWithRequestId() {
        Logger logger = Mockito.mock(Logger.class);
        LogUtils.logWarnWithRequestId(logger, "req-789", "Warn message", new RuntimeException("warn"));
        Mockito.verify(logger).warn(Mockito.eq("[{}] {}"), Mockito.eq("req-789"), Mockito.eq("Warn message"), Mockito.any(Throwable.class));
    }
} 