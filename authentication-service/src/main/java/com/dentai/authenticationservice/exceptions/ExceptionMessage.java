package com.dentai.authenticationservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ExceptionMessage {

    Logger logger = org.slf4j.LoggerFactory.getLogger(ExceptionMessage.class);

    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;


    public ExceptionMessage(String message){
        this.message = message;
        logger.info("ExceptionMessage: started with message | {}", message);
    }
    public ExceptionMessage(String message, int status) {
        this.message = message;
        this.status = status;
        logger.info("ExceptionMessage: started with message | {} and status | {}", message, status);
    }

    public ExceptionMessage(String date, int status, String reasonPhrase, String string, String url) {
        this.timestamp = date;
        this.status = status;
        this.error = reasonPhrase;
        this.message = string;
        this.path = url;
        logger.info("ExceptionMessage: started with date | {} and status | {} and reasonPhrase | {} and string | {} and url | {}", date, status, reasonPhrase, string, url);
    }
}
