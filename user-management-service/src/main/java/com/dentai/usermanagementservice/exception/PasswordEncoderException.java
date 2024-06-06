package com.dentai.usermanagementservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PasswordEncoderException extends BaseException{

    ExceptionMessage exceptionMessage;

    public PasswordEncoderException(String message) {
        super(message);
    }

    @Override
    public ResponseEntity<Object> getResponseEntity() {
        return new ResponseEntity<>(getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public PasswordEncoderException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.getMessage());
        this.exceptionMessage = exceptionMessage;
    }


}
