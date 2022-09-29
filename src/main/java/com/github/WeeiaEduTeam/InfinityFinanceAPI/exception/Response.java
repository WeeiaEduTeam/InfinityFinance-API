package com.github.WeeiaEduTeam.InfinityFinanceAPI.exception;

import org.springframework.http.HttpStatus;

enum Response {
    UNSUPPORTED_OPERATION(HttpStatus.NOT_IMPLEMENTED, HttpStatus.NOT_IMPLEMENTED.getReasonPhrase()),
    SUCCESS(HttpStatus.OK, HttpStatus.OK.getReasonPhrase()),
    VALIDATE_FAILED(HttpStatus.BAD_REQUEST, "Validation failed"),

    CONSTRAINT_VIOLATION(HttpStatus.CONFLICT, "The request could not be completed due to a conflict with the current state of the resource"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found"),

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
