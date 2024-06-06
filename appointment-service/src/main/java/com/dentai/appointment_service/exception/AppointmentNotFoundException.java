package com.dentai.appointment_service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class AppointmentNotFoundException extends BaseException{

    Logger logger = LoggerFactory.getLogger(AppointmentNotFoundException.class);

    public AppointmentNotFoundException(String message) {
        super(message);
        logger.error("AppointmentNotFoundException: created | {}", message);
    }

    public AppointmentNotFoundException(Long id) {
        super("Appointment not found by id: " + id);
        logger.error("AppointmentNotFoundException: created with id {} | {}", id, getMessage());

    }

    @Override
    public ResponseEntity<Object> getResponseEntity() {
        logger.error("AppointmentNotFoundException: getResponseEntity | {} and status: {}", getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(getMessage(), HttpStatus.NOT_FOUND);
    }
}
