package com.nttai.webflux.utils;

import com.nttai.webflux.dto.BaseResponse;
import com.nttai.webflux.dto.ErrorViolation;
import com.nttai.webflux.dto.Meta;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResponseUtilsTest {
    @Test
    void testSuccessWithData() {
        String data = "test-data";
        BaseResponse<String> response = ResponseUtils.success(data);
        assertEquals(data, response.getData());
        assertNotNull(response.getMeta());
        assertEquals(Meta.class, response.getMeta().getClass());
        assertEquals(BaseResponse.OK_CODE, response.getMeta().getCode());
    }

    @Test
    void testSuccessWithDataAndMessage() {
        String data = "test-data";
        String message = "Custom success";
        BaseResponse<String> response = ResponseUtils.success(data, message);
        assertEquals(data, response.getData());
        assertEquals(message, response.getMeta().getMessage());
    }

    @Test
    void testErrorWithList() {
        List<ErrorViolation> errors = Collections.singletonList(new ErrorViolation("field", "code", "desc", "msg"));
        BaseResponse<Void> response = ResponseUtils.error(400, "Bad Request", "req-1", errors);
        assertNull(response.getData());
        assertEquals(400, response.getMeta().getCode());
        assertEquals("Bad Request", response.getMeta().getMessage());
        assertEquals("req-1", response.getMeta().getMessageId());
        assertEquals(errors, response.getMeta().getErrors());
    }

    @Test
    void testErrorWithoutList() {
        BaseResponse<Void> response = ResponseUtils.error(500, "Error", "req-2");
        assertNull(response.getData());
        assertEquals(500, response.getMeta().getCode());
        assertEquals("Error", response.getMeta().getMessage());
        assertEquals("req-2", response.getMeta().getMessageId());
        assertNull(response.getMeta().getErrors());
    }
} 