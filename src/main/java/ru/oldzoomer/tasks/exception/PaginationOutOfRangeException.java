package ru.oldzoomer.tasks.exception;

public class PaginationOutOfRangeException extends RuntimeException {
    public PaginationOutOfRangeException(String message) {
        super(message);
    }
}
