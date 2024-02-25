package ru.sberbank.jd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<AppError> handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(new AppError("RESOURCE_NOT_FOUND", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleUnauthorizedOperationException(UnauthorizedOperationException e) {
        return new ResponseEntity<>(new AppError("UNAUTHORIZED", e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleInvalidParamsException(InvalidParamsException e) {
        return new ResponseEntity<>(new AppError("Запрос с некооректными параметрами", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
