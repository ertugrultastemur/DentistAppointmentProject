package com.dentai.usermanagementservice.exception;

import org.springframework.http.ResponseEntity;

public abstract class BaseException extends RuntimeException{

    public BaseException(String message) {
        super(message);
    }

    public abstract ResponseEntity<Object> getResponseEntity();
}
