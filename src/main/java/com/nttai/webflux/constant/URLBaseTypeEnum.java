package com.nttai.webflux.constant;

public enum URLBaseTypeEnum {
    URL_BASE_GET_POST("api1"),
    URL_BASE_GET_POST_DETAIL("api2"),
    ;
    private String value;

    URLBaseTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
