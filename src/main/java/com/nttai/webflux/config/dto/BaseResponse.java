package com.nttai.webflux.config.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nttai.webflux.config.constant.ErrorConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Slf4j
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BaseResponse<T> {

    public static final Integer OK_CODE = ErrorConstant.SUCCESS;

    public static final String SERVICE_CODE = "PG-A&F-SERVICE";

    private Meta meta = new Meta();

    private T data;

    public BaseResponse(Meta meta, T data) {
        this.meta = meta;
        this.data = data;
    }

    public BaseResponse(int code, String message, String messageId) {
        this.getMeta().setCode(code);
        this.getMeta().setMessage(message);
        this.getMeta().setMessageId(messageId);
        this.getMeta().setServiceCode(SERVICE_CODE);
    }
}