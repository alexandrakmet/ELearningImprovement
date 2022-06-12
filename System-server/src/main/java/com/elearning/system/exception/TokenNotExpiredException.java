package com.elearning.system.exception;

public class TokenNotExpiredException extends RuntimeException{
    public TokenNotExpiredException() {
    }

    public TokenNotExpiredException(String message) {
        super(message);
    }

    public TokenNotExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
