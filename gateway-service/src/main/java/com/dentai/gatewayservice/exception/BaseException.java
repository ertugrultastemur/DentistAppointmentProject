package com.dentai.gatewayservice.exception;

import org.springframework.http.ResponseEntity;

public abstract class BaseException  extends RuntimeException{


    protected BaseException(String message) {
        super(message);
    }

    public abstract ResponseEntity<Object> getResponseEntity();
}
