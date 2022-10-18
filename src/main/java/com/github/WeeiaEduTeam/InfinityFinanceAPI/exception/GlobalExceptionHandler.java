package com.github.WeeiaEduTeam.InfinityFinanceAPI.exception;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public ResponseEntity<Result<Map<String, String>>> handleConstraintViolationException(ConstraintViolationException exception) {

        Map<String, String> errors = new HashMap<>();
        errors.put("repository", exception.getMessage());

        log.error("Constraint violation: {}", errors);

        return Result.constraintViolation(errors);
    }

    @ExceptionHandler({ResourceNotFoundException.class, InvalidDataAccessApiUsageException.class})
    public @NotNull ResponseEntity<Result<Object>> handleUnknownResourceError(RuntimeException exception) {

        log.error(exception.getMessage());

        return Result.resourceNotFound();
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public @NotNull ResponseEntity<Result<Object>> handleForbiddenError(RuntimeException exception) {

        log.error(exception.getMessage());

        return Result.forbidden();
    }

    @ExceptionHandler({Exception.class})
    public @NotNull ResponseEntity<Result<Object>> handleUnknownError(Exception exception) {

        log.error(exception.getMessage());
        exception.printStackTrace();

        return Result.unknownError();
    }

}