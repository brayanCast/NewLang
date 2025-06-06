package com.newlang.backend.exceptions;

public class WordAlreadyExistException extends RuntimeException {
    public WordAlreadyExistException(String message) {
        super(message);
    }
}
