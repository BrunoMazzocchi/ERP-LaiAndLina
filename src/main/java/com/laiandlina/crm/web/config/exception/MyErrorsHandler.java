package com.laiandlina.crm.web.config.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import org.springframework.web.context.request.*;
import org.springframework.web.servlet.mvc.method.annotation.*;

@ControllerAdvice
public class MyErrorsHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    protected ResponseEntity<Object> handleAuthenticationError(RuntimeException ex, WebRequest request)
    {
        return handleExceptionInternal(ex,
                "Cannot login, please check your inputs",
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<Object> handlleServerError(RuntimeException ex, WebRequest request)
    {
        return handleExceptionInternal(ex,
                "Cannot login, error server",
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}