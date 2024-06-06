package com.dentai.usermanagementservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidUserDataException extends BaseException{

    Logger logger = LoggerFactory.getLogger(InvalidUserDataException.class);

    public InvalidUserDataException(String message) {
        super(message);
        logger.error("InvalidUserDataException: created | {}", message);
    }

    public InvalidUserDataException(Long id) {
        super("Invalid user data by id: " + id);
        logger.error("InvalidUserDataException: created with id {} | {}", id, getMessage());

    }

    @Override
    public ResponseEntity<Object> getResponseEntity() {
        logger.error("InvalidUserDataException: getResponseEntity | {} and status: {}", getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(getMessage(), HttpStatus.BAD_REQUEST);
    }
}
