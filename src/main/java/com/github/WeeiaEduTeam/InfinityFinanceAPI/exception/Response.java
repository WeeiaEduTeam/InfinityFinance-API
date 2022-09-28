package com.github.WeeiaEduTeam.InfinityFinanceAPI.exception;

import org.springframework.http.HttpStatus;

enum Response {
    UNSUPPORTED_OPERATION(-1, "Status code is unsupported"),
    SUCCESS(HttpStatus.OK, "OK"),
    VALIDATE_FAILED(HttpStatus.BAD_REQUEST, "Validation failed"),

    CONSTRAINT_VIOLATION(HttpStatus.CONFLICT, "The request could not be completed due to a conflict with the current state of the resource"),

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
