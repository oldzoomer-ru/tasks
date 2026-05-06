package ru.oldzoomer.tasks.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.oldzoomer.tasks.dto.output.Response;
import ru.oldzoomer.tasks.exception.CommentNotFoundException;
import ru.oldzoomer.tasks.exception.ForbiddenChangesException;
import ru.oldzoomer.tasks.exception.PaginationOutOfRangeException;
import ru.oldzoomer.tasks.exception.TaskNotFoundException;

@ControllerAdvice
@Log4j2
public class CustomExceptionResolver {

    @ExceptionHandler({CommentNotFoundException.class,
            PaginationOutOfRangeException.class, TaskNotFoundException.class})
    public ResponseEntity<Response<Object>> badRequestHandler(Throwable e) {
        log.error(e.getMessage());
        Response<Object> response = new Response<>(e.getMessage(), false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ForbiddenChangesException.class})
    public ResponseEntity<Response<Object>> forbiddenHandler(Throwable e) {
        log.error(e.getMessage());
        Response<Object> response = new Response<>(e.getMessage(), false);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

}
