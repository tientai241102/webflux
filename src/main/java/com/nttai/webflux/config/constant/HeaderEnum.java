package com.nttai.webflux.config.constant;

public enum HeaderEnum {

    X_REQUEST_ID("X-Request-ID", "requestId"),
    X_API_KEY("X-Api-Key", "apiKey"),
    X_API_SECRET("X-Api-Secret", "apiSecret"),
    X_LANG("X-Lang", "lang"),
    ;

    private String value;
    private String threadId;

    HeaderEnum(String value, String threadId) {
        this.value = value;
        this.threadId = threadId;
    }

    HeaderEnum() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }
}
