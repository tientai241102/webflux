package com.nttai.webflux.exception.handler;

import com.nttai.webflux.dto.BaseResponse;
import com.nttai.webflux.exception.BusinessException;
import com.nttai.webflux.utils.ErrorUtils;
import com.nttai.webflux.utils.LogUtils;
import com.nttai.webflux.utils.ResponseUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<BaseResponse<Void>>> handleApiException(BusinessException e) {
        return Mono.deferContextual(context -> {
            String requestId = getRequestId(context);
            LogUtils.logErrorWithRequestId(log, requestId, "Business Exception with status_code: " + e.getStatusCode() + " and error: " + e.getError(), e);
            return Mono.just(ResponseEntity.status(e.getStatusCode())
                .body(ResponseUtils.error(e.getStatusCode(), e.getError(), requestId)));
        });
    }

    @ExceptionHandler(WebClientResponseException.class)
    public Mono<ResponseEntity<BaseResponse<Void>>> handleWebClientResponseException(WebClientResponseException ex) {
        return Mono.deferContextual(context -> {
            String requestId = getRequestId(context);
            LogUtils.logErrorWithRequestId(log, requestId, "Third-party API error: " + ex.getMessage(), ex);
            return Mono.just(ResponseEntity.status(ex.getStatusCode())
                .body(ResponseUtils.error(ex.getStatusCode().value(), "ThirdPartyError", requestId)));
        });
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<BaseResponse<Void>>> handleValidationException(WebExchangeBindException ex) {
        return Mono.deferContextual(context -> {
            String requestId = getRequestId(context);
            LogUtils.logErrorWithRequestId(log, requestId, "Validation error: " + ex.getMessage(), ex);
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseUtils.error(HttpStatus.BAD_REQUEST.value(), "ValidationError", requestId, ErrorUtils.fromWebExchangeBindException(ex))));
        });
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<BaseResponse<Void>>> handleGenericException(Exception ex) {
        return Mono.deferContextual(context -> {
            String requestId = getRequestId(context);
            LogUtils.logErrorWithRequestId(log, requestId, "Unexpected error: " + ex.getMessage(), ex);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseUtils.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "InternalServerError", requestId, ErrorUtils.fromException(ex))));
        });
    }

    private String getRequestId(ContextView context){
        return  context.getOrDefault("requestId", "unknown");
    }
}