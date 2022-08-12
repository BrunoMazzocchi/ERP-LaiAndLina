package com.laiandlina.crm.web.config.exception;

import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Getter
@Setter
@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class TokenRefreshException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final String token;
    private final String message;

    public TokenRefreshException(String token, String message) {
        super(String.format("Couldn't refresh token for [%s]: [%s])", token, message));
        this.token = token;
        this.message = message;
    }
}