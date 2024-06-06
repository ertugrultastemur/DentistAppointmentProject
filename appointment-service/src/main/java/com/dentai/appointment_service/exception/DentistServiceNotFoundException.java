package com.dentai.appointment_service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class DentistServiceNotFoundException extends BaseException{
    Logger logger = LoggerFactory.getLogger(DentistServiceNotFoundException.class);

    public DentistServiceNotFoundException(String message) {
        super(message);
        logger.error("DentistServiceNotFoundException: created | {}", message);
    }

    public DentistServiceNotFoundException(Long id) {
        super("DentistService not found by id: " + id);
        logger.error("DentistServiceNotFoundException: created with id {} | {}", id, getMessage());

    }

    @Override
    public ResponseEntity<Object> getResponseEntity() {
        logger.error("AppointmentNotFoundException: getResponseEntity | {} and status: {}", getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(getMessage(), HttpStatus.NOT_FOUND);
    }
}
