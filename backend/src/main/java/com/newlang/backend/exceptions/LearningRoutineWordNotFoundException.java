package com.newlang.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LearningRoutineWordNotFoundException extends RuntimeException {
    public LearningRoutineWordNotFoundException(String message) {
        super(message);
    }
}
