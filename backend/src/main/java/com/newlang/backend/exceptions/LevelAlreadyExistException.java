package com.newlang.backend.exceptions;

public class LevelAlreadyExistException extends RuntimeException {
    public LevelAlreadyExistException(String message) {
        super(message);
    }
}
