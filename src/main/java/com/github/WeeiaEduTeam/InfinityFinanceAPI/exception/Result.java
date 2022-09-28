package com.github.WeeiaEduTeam.InfinityFinanceAPI.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> ResponseEntity<Result<T>> success(T data) {
        var cause = Response.SUCCESS;
        var result = getResult(cause, data);
        return getResultWithStatus(cause, result);
    }

    public static <T> ResponseEntity<Result<T>> failedValidation(T data) {
        var cause = Response.VALIDATE_FAILED;
        var result = getResult(cause, data);
        return getResultWithStatus(cause, result);
    }

    public static <T> ResponseEntity<Result<T>> constraintViolation(T data) {
        var cause = Response.CONSTRAINT_VIOLATION;
        var result = getResult(cause, data);
        return getResultWithStatus(cause, result);
    }

    public static @NotNull ResponseEntity<Result<String>> unknownError(String error) {
        var cause = Response.UNSUPPORTED_OPERATION;
        var result = getResult(cause, error);
        return getResultWithStatus(cause, result);
    }

    @NotNull
    private static <T> ResponseEntity<Result<T>> getResultWithStatus(Response response, Result<T> result) {
        return ResponseEntity.status(response.getStatus()).body(result);
    }

    @NotNull
    private static <T> Result<T> getResult(Response response, T data) {
        return new Result<>(response.getCode(), response.getMessage(), data);
    }
}