package com.nttai.webflux.exception;

import com.nttai.webflux.constant.ErrorEnum;
import com.nttai.webflux.utils.RequestContextUtils;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BusinessException extends RuntimeException {

    private Integer statusCode;
    private Integer errorCode;

    private String error;

    private String errorDescription;

    private String requestId;

    public BusinessException(int statusCode, String error, String errorDescription) {
        this.statusCode = statusCode;
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public BusinessException(ErrorEnum errorEnum, Object... params) {
        this.statusCode = errorEnum.getStatusCode();
        this.error = errorEnum.getError();

        if (StringUtils.isNotBlank(errorEnum.getErrorDescription())) {
            this.errorDescription = String.format(errorEnum.getErrorDescription(), params);
        }
        this.requestId = RequestContextUtils.getRequestId().toString();

    }

    public BusinessException(ErrorEnum errorEnum) {
        this.statusCode = errorEnum.getStatusCode();
        this.errorCode = errorEnum.getErrorCode() == null ? errorEnum.getStatusCode() : errorEnum.getErrorCode();
        this.error = errorEnum.getError();
        this.errorDescription = errorEnum.getErrorDescription();
        this.requestId = RequestContextUtils.getRequestId().toString();
    }
}