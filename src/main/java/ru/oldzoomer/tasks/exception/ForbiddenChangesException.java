package ru.oldzoomer.tasks.exception;

public class ForbiddenChangesException extends RuntimeException {
    public ForbiddenChangesException(String message) {
        super(message);
    }
}
