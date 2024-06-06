package com.dentai.authenticationservice.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public abstract class BaseException extends RuntimeException{

    Logger logger = org.slf4j.LoggerFactory.getLogger(BaseException.class);


    protected BaseException(String message){
        super(message);
        logger.info("BaseException: started with message | {}", message);
    }


    public abstract ResponseEntity<Object> getResponseEntity();


}
