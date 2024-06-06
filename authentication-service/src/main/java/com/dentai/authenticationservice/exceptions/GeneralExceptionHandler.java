package com.dentai.authenticationservice.exceptions;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralExceptionHandler {

    Logger logger = org.slf4j.LoggerFactory.getLogger(GeneralExceptionHandler.class);

    @ExceptionHandler(TokenNotValidException.class)
    public ResponseEntity<?> handle(TokenNotValidException exception){
        logger.error("GeneralExceptionHandler: handle started with exception | {}", exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> handle(BaseException exception){
        logger.error("GeneralExceptionHandler: handle started with exception | {}", exception.getMessage());
        return new ResponseEntity<>(exception.getResponseEntity(), exception.getResponseEntity().getStatusCode());
    }


}
