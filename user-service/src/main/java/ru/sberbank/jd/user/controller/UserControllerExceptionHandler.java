package ru.sberbank.jd.user.controller;

import java.util.NoSuchElementException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.sberbank.api.user.service.dto.ErrorResponse;

/**
 * Exception handler for HTTP statuses.
 */
@RestControllerAdvice
public class UserControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException exception) {
        return new ResponseEntity<>(setErrorMessage(exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElement(NoSuchElementException exception) {
        return new ResponseEntity<>(setErrorMessage(exception), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException exception) {
        return new ResponseEntity<>(setErrorMessage(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ErrorResponse error = new ErrorResponse();

        for (FieldError message : exception.getBindingResult().getFieldErrors()) {
            error.getMessages().add(message.getField() + ": " + message.getDefaultMessage());
        }
        for (ObjectError message : exception.getBindingResult().getGlobalErrors()) {
            error.getMessages().add(message.getObjectName() + ": " + message.getDefaultMessage());
        }

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse setErrorMessage(Exception exception) {
        ErrorResponse error = new ErrorResponse();
        error.getMessages().add(exception.getMessage());
        return error;
    }
}
