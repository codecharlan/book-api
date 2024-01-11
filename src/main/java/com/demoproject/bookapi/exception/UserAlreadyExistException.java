package com.demoproject.bookapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class UserAlreadyExistException extends RuntimeException {
    private final HttpStatus status;
    public UserAlreadyExistException(String s) {
        super(s);
        this.status = HttpStatus.CONFLICT;
    }
}
