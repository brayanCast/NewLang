package com.newlang.backend.exceptions;

public class ExpressionAlreadyExistException extends RuntimeException {
    public ExpressionAlreadyExistException(String message) {
        super(message);
    }
}
