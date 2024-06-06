package com.dentai.usermanagementservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistException extends BaseException {

    Logger logger = LoggerFactory.getLogger(UserAlreadyExistException.class);

    public UserAlreadyExistException(String message) {
        super(message);
        logger.error("UserAlreadyExistException: created | {}", message);
    }

    public UserAlreadyExistException(Long id) {
        super("User already exists by id: " + id);
        logger.error("UserAlreadyExistException: created with id {} | {}", id, getMessage());

    }

    @Override
    public ResponseEntity<Object> getResponseEntity() {
        logger.error("UserAlreadyExistException: getResponseEntity | {} and status: {}", getMessage(), HttpStatus.CONFLICT);
        return new ResponseEntity<>(getMessage(), HttpStatus.CONFLICT);
    }
}
