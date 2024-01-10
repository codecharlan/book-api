package com.demoproject.bookapi.exception;

import com.demoproject.bookapi.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(NOT_ACCEPTABLE)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(propertyPath, message);
        });
        return new ResponseEntity<>(new ApiResponse<>("Validation Failed", errors, NOT_ACCEPTABLE.value()), NOT_ACCEPTABLE);
    }
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), BAD_REQUEST.value()), BAD_REQUEST);
    }

    @ExceptionHandler(BookAlreadyCreatedException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleBookAlreadyCreatedException(BookAlreadyCreatedException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), BAD_REQUEST.value()), BAD_REQUEST);
    }

    @ExceptionHandler(BookCannotBeDeletedException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleBookCannotBeDeletedException(BookCannotBeDeletedException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), BAD_REQUEST.value()), BAD_REQUEST);
    }
    @ExceptionHandler(BookNotAvailableException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleBookNotAvailableException(BookNotAvailableException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), BAD_REQUEST.value()), BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), BAD_REQUEST.value()), BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleIllegalStateException(IllegalStateException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), BAD_REQUEST.value()), BAD_REQUEST);
    }
}
