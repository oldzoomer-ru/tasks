package ru.gavrilovegor519.tasks.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.gavrilovegor519.tasks.dto.output.Response;
import ru.gavrilovegor519.tasks.exception.*;

@ControllerAdvice
@Log4j2
public class CustomExceptionResolver {

    @ExceptionHandler({CommentNotFoundException.class,
            PaginationOutOfRangeException.class, TaskNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<Response> badRequestHandler(Throwable e) {
        log.error(e.getMessage());
        Response response = new Response(e.getMessage(), false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<Response> conflictHandler(Throwable e) {
        log.error(e.getMessage());
        Response response = new Response(e.getMessage(), false);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ForbiddenChangesException.class,
            IncorrectPasswordException.class})
    public ResponseEntity<Response> forbiddenHandler(Throwable e) {
        log.error(e.getMessage());
        Response response = new Response(e.getMessage(), false);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Response> otherHandler(Throwable e) {
        log.error("Unexpected error occurred", e);
        Response response = new Response("An unexpected error occurred", false);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
