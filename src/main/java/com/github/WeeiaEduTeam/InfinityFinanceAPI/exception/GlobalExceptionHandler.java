package com.github.WeeiaEduTeam.InfinityFinanceAPI.exception;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "com.github.WeeiaEduTeam.InfinityFinanceAPI")
class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Result<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });

        log.error("Validation failed: {}", errors);

        return Result.failedValidation(errors);
    }


    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Result<Map<String, String>>> handleConstraintViolationException(ConstraintViolationException ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("repository", ex.getMessage());

        log.error("Constraint violation: {}", errors);

        return Result.constraintViolation(errors);
    }

    @ExceptionHandler({Exception.class})
    public @NotNull ResponseEntity<Result<String>> handleUnknownError(Exception exception) {
        return Result.unknownError(exception.getMessage());
    }

}