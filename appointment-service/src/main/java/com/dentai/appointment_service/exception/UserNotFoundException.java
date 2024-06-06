package com.dentai.authenticationservice.exceptions;

import com.dentai.appointment_service.exception.BaseException;
import com.dentai.appointment_service.exception.ExceptionMessage;
import lombok.Getter;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends BaseException {

    Logger logger = org.slf4j.LoggerFactory.getLogger(UserNotFoundException.class);
    private ExceptionMessage exceptionMessage;

    public UserNotFoundException(ExceptionMessage exceptionMessage){
        super(exceptionMessage.getMessage());
        logger.error("UserNotFoundException: started with message | {}", exceptionMessage.getMessage());
        this.exceptionMessage = exceptionMessage;
    }
    public UserNotFoundException(String message) {
        super(message);
        logger.error("UserNotFoundException: started with message | {}", message);
    }

    @Override
    public ResponseEntity<Object> getResponseEntity() {
        logger.error("UserNotFoundException: getResponseEntity started with message | {} status | {}", getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(getMessage(), HttpStatus.NOT_FOUND);
    }


}
