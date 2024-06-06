package com.dentai.usermanagementservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MedicineNotFoundException extends BaseException{

    Logger logger = LoggerFactory.getLogger(MedicineNotFoundException.class);

    ExceptionMessage exceptionMessage;

    public MedicineNotFoundException(String message) {
        super(message);
        logger.error("MedicineNotFoundException: created | {}", message);
    }

    public MedicineNotFoundException(Long id) {
        super("Prescription not found by id: " + id);
        logger.error("MedicineNotFoundException: created with id {} | {}", id, getMessage());

    }

    @Override
    public ResponseEntity<Object> getResponseEntity() {
        logger.error("MedicineNotFoundException: getResponseEntity | {} and status: {}", getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(getMessage(), HttpStatus.NOT_FOUND);
    }


}
