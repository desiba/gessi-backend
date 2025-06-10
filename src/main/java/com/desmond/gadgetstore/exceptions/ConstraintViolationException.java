package com.desmond.gadgetstore.exceptions;


public class ConstraintViolationException extends RuntimeException{
    public ConstraintViolationException() {
    }
    public ConstraintViolationException(String message) {
        super(message);
    }
}
