package com.dentai.usermanagementservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends BaseException {

    Logger logger = LoggerFactory.getLogger(UserNotFoundException.class);

    ExceptionMessage exceptionMessage;

    public UserNotFoundException(String message) {
        super(message);
        logger.error("UserNotFoundException: created | {}", message);
    }

    public UserNotFoundException(Long id) {
        super("User not found by id: " + id);
        logger.error("UserNotFoundException: created with id {} | {}", id, getMessage());

    }

    @Override
    public ResponseEntity<Object> getResponseEntity() {
        logger.error("UserNotFoundException: getResponseEntity | {} and status: {}", getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(getMessage(), HttpStatus.NOT_FOUND);
    }


}
