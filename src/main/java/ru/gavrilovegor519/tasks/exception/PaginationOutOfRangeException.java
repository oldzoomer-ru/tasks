package ru.gavrilovegor519.tasks.exception;

public class PaginationOutOfRangeException extends RuntimeException {
    public PaginationOutOfRangeException(String message) {
        super(message);
    }
}
