package com.elearning.system.exception;

public class LoginAlreadyExistsException extends RuntimeException {
    public LoginAlreadyExistsException() {
    }

    public LoginAlreadyExistsException(String message) {
        super(message);
    }

    public LoginAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
