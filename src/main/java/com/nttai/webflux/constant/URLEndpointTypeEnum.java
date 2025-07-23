package com.nttai.webflux.constant;

public enum URLEndpointTypeEnum {
    URL_ENDPOINT_GET_POST("/posts"),
    URL_ENDPOINT_GET_POST_DETAIL("/posts/%s"),
    ;
    private String value;

    URLEndpointTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
