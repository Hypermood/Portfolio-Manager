package com.melon.portfoliomanager.exceptions;

public class NoSuchUserException extends RuntimeException {

    public NoSuchUserException(String msg) {
        super(msg);
    }
}
