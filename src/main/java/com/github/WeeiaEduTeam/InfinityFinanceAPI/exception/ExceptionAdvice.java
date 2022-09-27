package com.github.WeeiaEduTeam.InfinityFinanceAPI.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.github.WeeiaEduTeam.InfinityFinanceAPI")
public class ExceptionAdvice {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Result<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });


        return Result.failedValidation(errors);
    }


    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Result<String>> handleConstraintViolationException(ConstraintViolationException ex) {

        if (StringUtils.hasText(ex.getMessage())) {
            Result.failedValidation("errors");
        }

        return Result.failedValidation("errors");
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Result<String>> handle(Exception ex) {
        return Result.failedValidation("errors");
    }

}