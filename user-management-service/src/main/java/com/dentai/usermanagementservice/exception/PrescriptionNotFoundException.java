package com.dentai.usermanagementservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PrescriptionNotFoundException extends BaseException {

    Logger logger = LoggerFactory.getLogger(PrescriptionNotFoundException.class);

    ExceptionMessage exceptionMessage;

    public PrescriptionNotFoundException(String message) {
        super(message);
        logger.error("PrescriptionNotFoundException: created | {}", message);
    }

    public PrescriptionNotFoundException(Long id) {
        super("Prescription not found by id: " + id);
        logger.error("PrescriptionNotFoundException: created with id {} | {}", id, getMessage());

    }

    @Override
    public ResponseEntity<Object> getResponseEntity() {
        logger.error("PrescriptionNotFoundException: getResponseEntity | {} and status: {}", getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(getMessage(), HttpStatus.NOT_FOUND);
    }
}
