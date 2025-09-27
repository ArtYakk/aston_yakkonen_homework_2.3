package com.hibernateapp.exception;

public class UserUpdateException extends RuntimeException {
    public UserUpdateException(String message, Exception e) {
        super(message, e);
    }
}
