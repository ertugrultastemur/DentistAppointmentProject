package com.dentai.gatewayservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralExceptionHandler {


    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> handle(TokenUnauthorizedException exception){
        return new ResponseEntity<>(exception.getResponseEntity(), HttpStatus.UNAUTHORIZED);
    }
}
