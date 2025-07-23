package com.nttai.webflux.config.constant;

public class ErrorConstant {

    private ErrorConstant() {}

    /**
     * Write the error code prefixed with 200 below
     * 200
     */
    public static final int SUCCESS = 200000;

    /**
     * Write the error code prefixed with 400 below
     * 400
     */
    public static final int INVALID_PARAMETERS = 4000001;

    /**
     * Write the error code prefixed with 401 below
     * 401
     */
    public static final int UNAUTHORIZED = 4010001;

    /**
     * Write the error code prefixed with 403 below
     * 403
     */
    public static final int FORBIDDEN_ERROR = 4030001;

    /**
     *  Write the error code prefixed with 404 below
     * 404
     */
    public static final int NOT_FOUND = 4040001;


    /**
     * Write the error code prefixed with 500 below
     * 500
     */
    public static final int INTERNAL_SERVER_ERROR = 5001001;
}