package com.github.WeeiaEduTeam.InfinityFinanceAPI.exception;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice(basePackages = "com.github.WeeiaEduTeam.InfinityFinanceAPI")
public class ExceptionAdvice {

    /*@ExceptionHandler({MethodArgumentNotValidException.class})
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        BindingResult bindingResult = ex.getBindingResult();

        StringBuilder sb = new StringBuilder("valid err:");

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
        }

        String msg = sb.toString();

        if (StringUtils.hasText(msg)) {
            return Result.failed(-11, msg);
        }

        return Result.failed(-12, "MethodArgumentNotValid");
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public Result<?> handleConstraintViolationException(ConstraintViolationException ex) {

        if (StringUtils.hasText(ex.getMessage())) {
            return Result.failed(-13, ex.getMessage());
        }

        return Result.failed(-14, "ConstraintViolation");
    }

    @ExceptionHandler({Exception.class})
    public Result<?> handle(Exception ex) {
        return Result.failed(-9, ex.getMessage());
    }*/

}