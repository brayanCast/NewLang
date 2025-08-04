package com.newlang.backend.exceptions;

public class LearningRoutineAlreadyExistException extends RuntimeException {
    public LearningRoutineAlreadyExistException(String message) {
        super(message);
    }
}
