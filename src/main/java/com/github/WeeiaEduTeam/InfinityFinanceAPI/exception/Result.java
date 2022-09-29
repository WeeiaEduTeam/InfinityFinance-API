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
        return getWrappedResultResponseEntity(data, cause);
    }

    public static <T> ResponseEntity<Result<T>> failedValidation(T data) {
        var cause = Response.VALIDATE_FAILED;
        return getWrappedResultResponseEntity(data, cause);
    }

    public static <T> ResponseEntity<Result<T>> constraintViolation(T data) {
        var cause = Response.CONSTRAINT_VIOLATION;
        return getWrappedResultResponseEntity(data, cause);
    }

    public static @NotNull ResponseEntity<Result<Object>> unknownError() {
        var cause = Response.UNSUPPORTED_OPERATION;
        return getWrappedResultResponseEntity(cause);
    }

    public static @NotNull ResponseEntity<Result<Object>> resourceNotFound() { // dac nulla np
        var cause = Response.NOT_FOUND;
        return getWrappedResultResponseEntity(cause);
    }

    private static @NotNull <T> ResponseEntity<Result<Object>> getWrappedResultResponseEntity(Response cause) {
        var result = getResult(cause, null);
        return getResultWithStatus(cause, result);
    }

    @NotNull
    private static <T> ResponseEntity<Result<T>> getWrappedResultResponseEntity(T data, Response cause) {
        var result = getResult(cause, data);
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