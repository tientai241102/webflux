package com.nttai.webflux.config.utils;

import com.nttai.webflux.config.dto.BaseResponse;
import com.nttai.webflux.config.dto.ErrorViolation;
import com.nttai.webflux.config.dto.Meta;

import java.util.List;

public class ResponseUtils {
    public static <T> BaseResponse<T> success(T data, String message) {
        Meta meta = new Meta();
        meta.setCode(BaseResponse.OK_CODE);
        meta.setMessage(message);
        meta.setServiceCode(BaseResponse.SERVICE_CODE);
        meta.setTimestamp(System.currentTimeMillis());
        return new BaseResponse<>(meta, data);
    }

    public static <T> BaseResponse<T> success(T data) {
        return success(data, "Success");
    }

    public static BaseResponse<Void> error(int code, String message, String messageId, List<?> errors) {
        Meta meta = new Meta();
        meta.setCode(code);
        meta.setMessage(message);
        meta.setMessageId(messageId);
        meta.setServiceCode(BaseResponse.SERVICE_CODE);
        meta.setTimestamp(System.currentTimeMillis());
        if (errors != null && errors.get(0) != null && errors.get(0) instanceof ErrorViolation) {
            meta.setErrors((List<ErrorViolation>) errors);
        }
        return new BaseResponse<>(meta, null);
    }

    public static BaseResponse<Void> error(int code, String message, String messageId) {
        return error(code, message, messageId, null);
    }
} 