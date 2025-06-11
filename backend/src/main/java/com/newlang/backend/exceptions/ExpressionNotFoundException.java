package com.newlang.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExpressionNotFoundException extends RuntimeException {
    public ExpressionNotFoundException(String message) {
        super(message);
    }

    public ExpressionNotFoundException(String message, Throwable cause) {super(message, cause);}
}
