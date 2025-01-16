package org.example.bookstorageservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<ErrorMessage> badRequest(
            BadRequest exception) {
        ErrorMessage message = new ErrorMessage(exception.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFound.class)
    public ResponseEntity<ErrorMessage> notFound(
            NotFound exception) {
        ErrorMessage message = new ErrorMessage(exception.getMessage());
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Unauthorized.class)
    public ResponseEntity<ErrorMessage> unauthorized(
            Unauthorized exception) {
        ErrorMessage message = new ErrorMessage(exception.getMessage());
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }
}
