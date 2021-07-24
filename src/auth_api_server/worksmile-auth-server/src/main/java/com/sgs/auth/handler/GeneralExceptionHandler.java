package com.sgs.auth.handler;

import com.sgs.auth.dto.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
@Slf4j
public class GeneralExceptionHandler {

    @ExceptionHandler({
            IllegalStateException.class, IllegalArgumentException.class,
            TypeMismatchException.class, HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class, MultipartException.class,
            JSONException.class
    })
    public ApiResult handleBadRequestException(Exception e) {
        log.debug("Bad Request Exception Occurred: {}", e.getMessage(), e);
        return new ApiResult(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    }
}
