package com.nttai.webflux.utils;

import com.nttai.webflux.dto.ErrorViolation;
import org.junit.jupiter.api.Test;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ErrorUtilsTest {
    @Test
    void testFromException() {
        Exception ex = new Exception("Test error");
        List<ErrorViolation> violations = ErrorUtils.fromException(ex);
        assertEquals(1, violations.size());
        assertEquals("Test error", violations.get(0).getDescription());
    }

    @Test
    void testFromWebExchangeBindException() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objectName");
        bindingResult.addError(new FieldError("objectName", "field1", "Field1 is required"));
        bindingResult.addError(new FieldError("objectName", "field2", "Field2 is invalid"));
        WebExchangeBindException ex = new WebExchangeBindException(null, bindingResult);
        List<ErrorViolation> violations = ErrorUtils.fromWebExchangeBindException(ex);
        assertEquals(2, violations.size());
        assertEquals("field1", violations.get(0).getField());
        assertEquals("Field1 is required", violations.get(0).getDescription());
        assertEquals("field2", violations.get(1).getField());
        assertEquals("Field2 is invalid", violations.get(1).getDescription());
    }
} 