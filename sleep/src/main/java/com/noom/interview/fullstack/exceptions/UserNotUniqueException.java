package com.noom.interview.fullstack.exceptions;

public class UserNotUniqueException extends Throwable {
    public UserNotUniqueException(String message) {
        super(message);
    }
}
