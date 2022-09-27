package com.github.WeeiaEduTeam.InfinityFinanceAPI.exception;

import org.springframework.http.HttpStatus;

public enum Response {
    SUCCESS(HttpStatus.OK, "OK"),
    VALIDATE_FAILED(HttpStatus.BAD_REQUEST, "Validation failed"),
    UNSUPPORTED_OPERATION(-1, "Status code is unsupported")
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;

    Response(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.code = httpStatus.value();
        this.message = message;
    }

    Response(int code, String message) {
        this.httpStatus = HttpStatus.I_AM_A_TEAPOT;
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }
    public HttpStatus getStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
