package com.github.WeeiaEduTeam.InfinityFinanceAPI.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        return new Result<>(Response.SUCCESS.getCode(), Response.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(Response.SUCCESS.getCode(), msg, data);
    }

    public static <T> ResponseEntity<Result<T>> failedValidation(T data) {
        var result = new Result<>(Response.VALIDATE_FAILED.getCode(), Response.VALIDATE_FAILED.getMessage(), data);

        return ResponseEntity.status(Response.VALIDATE_FAILED.getStatus()).body(result);
    }
}