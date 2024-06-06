package com.dentai.authenticationservice.exceptions;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnsignedJwtException extends BaseException {

    Logger logger = org.slf4j.LoggerFactory.getLogger(UnsignedJwtException.class);

    HttpStatus statusCode = HttpStatus.UNAUTHORIZED;
    public UnsignedJwtException(String message) {
        super(message);
        logger.error("UnsignedJwtException: started with message | {}", message);
    }

    @Override
    public ResponseEntity<Object> getResponseEntity() {
        logger.error("UnsignedJwtException: getResponseEntity started with message | {} status | {}", getMessage(), statusCode);
        return new ResponseEntity<>(getMessage(), statusCode);
    }


}
