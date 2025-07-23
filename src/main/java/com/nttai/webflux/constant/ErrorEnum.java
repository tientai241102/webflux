package com.nttai.webflux.constant;

import org.springframework.http.HttpStatus;

public enum ErrorEnum {
    SUCCESS(HttpStatus.OK.value(), "SUCCESS", "Success"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "Bad_Request", "Bad request"),
    INTERNAL_SERVER_ERROR(500, "Internal_Server_Error", "Internal server error"),

    THIRD_PARTY_ERROR(500, "THIRD_PARTY_ERROR", "Third party error"),

    NO_POST(500, "NO_POST", "No posts found"),
    ;
    private int statusCode;
    private Integer errorCode;
    private String error;
    private String errorDescription;

    ErrorEnum(int statusCode, String error) {
        this.statusCode = statusCode;
        this.error = error;
    }

    ErrorEnum(int statusCode, String error, String errorDescription) {
        this.statusCode = statusCode;
        this.error = error;
        this.errorDescription = errorDescription;
    }

    ErrorEnum(int statusCode, Integer errorCode, String error, String errorDescription) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String message) {
        this.errorDescription = errorDescription;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}


