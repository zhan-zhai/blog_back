package com.zdz.handler.exception;

import com.zdz.domain.ResponseResult;
import com.zdz.enums.AppHttpCodeEnum;
import com.zdz.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler{
    @ExceptionHandler(value = Exception.class)
    public ResponseResult<?> globalExceptionHandler(Exception e){
        log.error("发生其他异常！msg: ->",e);
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }

    @ExceptionHandler(value = SystemException.class)
    public ResponseResult<?> systemExceptionHandler(SystemException e){
        log.error("发生业务异常！msg: ->",e);
        return ResponseResult.errorResult(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseResult<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        log.error("参数校验异常！msg: ->",e);
        String message = e.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError ->
                        String.format("[%s: %s]", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining());

        return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_CHECK_ERROR.getCode(),message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseResult<?> ConstraintViolationExceptionHandler(ConstraintViolationException e) {
        log.error("参数校验异常！msg: ->", e);
        String message = e.getConstraintViolations().stream().map(constraintViolation->
                String.format("[%s: %s]", constraintViolation.getConstraintDescriptor(), constraintViolation.getMessage())).collect(Collectors.joining());
        return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_CHECK_ERROR.getCode(),message);
    }
}