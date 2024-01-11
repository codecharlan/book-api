package com.demoproject.bookapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class UserNotFoundException extends RuntimeException {
    private final HttpStatus status;
    public UserNotFoundException(String s, HttpStatus status) {
        super(s);
        this.status = status;
    }
}
