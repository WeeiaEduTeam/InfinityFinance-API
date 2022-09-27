package com.github.WeeiaEduTeam.InfinityFinanceAPI.exception;

public enum Response {
    SUCCESS(0, "ok"),
    VALIDATE_FAILED(-1, "validate failed"),
    ;

    private final Integer code;
    private final String message;

    Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return message;
    }
}
