package com.desmond.gadgetstore.exceptions;


public class ConstraintViolationException extends RuntimeException{
    private static final long serialVersionUID = 1L;
	public ConstraintViolationException() {
    }
    public ConstraintViolationException(String message) {
        super(message);
    }
}
