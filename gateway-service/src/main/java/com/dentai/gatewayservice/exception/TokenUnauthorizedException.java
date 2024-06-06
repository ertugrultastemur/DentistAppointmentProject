package com.dentai.gatewayservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TokenUnauthorizedException extends BaseException{
    public TokenUnauthorizedException(String message) {
        super(message);
    }

    @Override
    public ResponseEntity<Object> getResponseEntity() {
        return new ResponseEntity<>(this.getMessage(), HttpStatus.UNAUTHORIZED);
    }


}
