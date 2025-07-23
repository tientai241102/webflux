package com.nttai.webflux.utils;

import com.nttai.webflux.dto.ErrorViolation;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ErrorUtils {
    private ErrorUtils() {}

    public static List<ErrorViolation> fromWebExchangeBindException(WebExchangeBindException ex) {
        if (ex == null || ex.getFieldErrors() == null) return Collections.emptyList();
        return ex.getFieldErrors().stream()
                .map(fieldError -> new ErrorViolation(
                        fieldError.getField(),
                        fieldError.getCode(),
                        fieldError.getDefaultMessage(),
                        fieldError.getDefaultMessage()
                ))
                .collect(Collectors.toList());
    }

    public static List<ErrorViolation> fromException(Exception ex) {
        if (ex == null) return Collections.emptyList();
        return Collections.singletonList(
                new ErrorViolation(null, null, ex.getMessage(), ex.getMessage())
        );
    }
} 