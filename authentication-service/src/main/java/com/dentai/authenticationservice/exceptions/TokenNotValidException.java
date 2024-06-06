package com.dentai.authenticationservice.exceptions;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class TokenNotValidException extends BaseException {

    Logger logger = org.slf4j.LoggerFactory.getLogger(TokenNotValidException.class);

    public TokenNotValidException(String message) {
        super(message);
        logger.error("TokenNotValidException: started with message | {}", message);
    }

    @Override
    public ResponseEntity<Object> getResponseEntity() {
        logger.error("TokenNotValidException: getResponseEntity started with message | {} status | {}", getMessage(), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(this.getMessage(), HttpStatus.FORBIDDEN);
    }


}