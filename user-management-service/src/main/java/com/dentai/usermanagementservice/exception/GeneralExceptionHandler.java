package com.dentai.usermanagementservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);


    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handle(BaseException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), e.getResponseEntity().getStatusCode());
    }
}
