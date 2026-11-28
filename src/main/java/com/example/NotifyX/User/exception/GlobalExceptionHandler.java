package com.example.NotifyX.User.exception;

import com.example.NotifyX.preference.exception.PreferenceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ApiError(String message) {}

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            UserNotFoundException exception) {

        return buildErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleDuplicate(
            UserAlreadyExistsException exception) {

        return buildErrorMessage(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDatabaseConflict(
            DataIntegrityViolationException exception) {

        return buildErrorMessage(HttpStatus.CONFLICT, "Database constraint violation");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> buildErrorMessage(
            MethodArgumentNotValidException exception) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Invalid request");

        return buildErrorMessage(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(PreferenceNotFoundException.class)
    public ResponseEntity<ApiError> handlePreferenceNotFound(
            PreferenceNotFoundException ex
    ) {
        return buildErrorMessage(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    public ResponseEntity<ApiError> buildErrorMessage(HttpStatus status, String errorMessage){
        return ResponseEntity.status(status)
                .body(new ApiError(errorMessage));
    }
}
