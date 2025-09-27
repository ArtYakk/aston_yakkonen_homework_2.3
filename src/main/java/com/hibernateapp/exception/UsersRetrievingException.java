package com.hibernateapp.exception;

public class UsersRetrievingException extends RuntimeException {
    public UsersRetrievingException(String message, Exception e) {
        super(message, e);
    }
}
