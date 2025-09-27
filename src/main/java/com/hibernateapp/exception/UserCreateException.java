package com.hibernateapp.exception;

public class UserCreateException extends RuntimeException {
    public UserCreateException(String message, Exception e) {
        super(message, e);
    }
}
