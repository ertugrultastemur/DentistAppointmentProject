package com.dentai.usermanagementservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class XRayNotFoundException extends BaseException{

    public XRayNotFoundException(Long id) {
        super("XRay not found by id: " + id);
    }

    public XRayNotFoundException(String message) {
        super(message);
    }

    @Override
    public ResponseEntity<Object> getResponseEntity() {
        return new ResponseEntity<>(this.getMessage(), HttpStatus.NOT_FOUND);
    }
}
