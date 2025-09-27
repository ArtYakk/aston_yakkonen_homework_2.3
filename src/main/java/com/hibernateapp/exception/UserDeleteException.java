package com.hibernateapp.exception;

public class UserDeleteException extends RuntimeException {
    public UserDeleteException(String message, Exception e) {
        super(message, e);
    }
}
