package com.elearning.system.exception;

public class MailAlreadyExistsException extends RuntimeException {

    public MailAlreadyExistsException(String message) {
        super(message);
    }

}
