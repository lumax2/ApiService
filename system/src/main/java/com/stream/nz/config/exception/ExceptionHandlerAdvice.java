package com.stream.nz.config.exception;

import com.stream.nz.constant.ResultData;
import com.stream.nz.token.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Objects;

import static com.stream.nz.constant.ResponseDataConstant.STATUS_FAILURE;


@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultData<Void> handleMethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String errorMessage;
        try {
            errorMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        } catch (NullPointerException npe) {
            errorMessage = "MethodArgumentNotValidException";
        }

        return new ResultData<Void>()
                .setStatus(STATUS_FAILURE)
                .setErrMsg(errorMessage);
    }

    @ExceptionHandler(AdminException.class)
    public ResultData<Void> handleMethodAdminExceptionHandler(AdminException e) {
        if (log.isErrorEnabled()) {
            log.error("error detail：{} \n", e.getMessage(), e);
        }

        String errorMessage;
        try {
            errorMessage = Objects.requireNonNull(e.getMessage());
        } catch (NullPointerException npe) {
            errorMessage = "AdminException";
        }

        return new ResultData<Void>()
                .setStatus(STATUS_FAILURE)
                .setErrMsg(errorMessage);
    }



    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResultData<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        if (log.isErrorEnabled()) {
            log.error("error detail：{} \n", e.getMessage(), e);
        }

        return new ResultData<Void>()
                .setStatus(STATUS_FAILURE)
                .setErrMsg(e.getParameterName() + "MissingServletRequestParameterException");
    }

    @ExceptionHandler(BindException.class)
    public ResultData<Void> handleBindExceptionHandler(BindException e) {
        if (log.isErrorEnabled()) {
            log.error("error detail：{} \n", e.getMessage(), e);
        }

        String errorMessage;
        try {
            errorMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        } catch (NullPointerException npe) {
            errorMessage = "BindException";
        }

        return new ResultData<Void>()
                .setStatus(STATUS_FAILURE)
                .setErrMsg(errorMessage);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultData<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        if (log.isErrorEnabled()) {
            log.error("exception detail：{} \n", e.getMessage(), e);
        }

        return new ResultData<Void>()
                .setStatus(STATUS_FAILURE)
                .setErrMsg(String.format("Method not support：[%s]，should be: %s", e.getMethod(), Arrays.toString(e.getSupportedMethods())));
    }

    @ExceptionHandler(Exception.class)
    public ResultData<Void> handleException(Exception e) {
        if (log.isErrorEnabled()) {
            log.error("exception detail：{} \n", e.getMessage(), e);
        }

        return new ResultData<Void>()
                .setStatus(STATUS_FAILURE)
                .setErrMsg("Unknown Exception");
    }

    @ExceptionHandler(AuthException.class)
    public ResultData<Void> handleException(AuthException e) {
        if (log.isErrorEnabled()) {
            log.error("exception detail：{} \n", e.getMessage(), e);
        }
        return new ResultData<Void>()
                .setStatus(STATUS_FAILURE)
                .setErrMsg("jwt wrong or expired");
    }
}
