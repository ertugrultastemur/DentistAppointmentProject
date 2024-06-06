package com.dentai.authenticationservice.exceptions;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SecurityContextException extends BaseException {

    Logger logger = org.slf4j.LoggerFactory.getLogger(SecurityContextException.class);

    public SecurityContextException(String message) {
        super(message);
        logger.error("SecurityContextException: started with message | {}", message);
    }

    @Override
    public ResponseEntity<Object> getResponseEntity() {
        logger.error("SecurityContextException: getResponseEntity started with message | {}", getMessage());
        return new ResponseEntity<>(getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
