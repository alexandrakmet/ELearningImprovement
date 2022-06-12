package com.elearning.system.exception;

public class TokenNotExpiredException extends RuntimeException{

    public TokenNotExpiredException(String message) {
        super(message);
    }

}
