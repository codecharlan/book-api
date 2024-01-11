package com.demoproject.bookapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class InvalidCredentialException extends RuntimeException {
    private final HttpStatus status;
    public InvalidCredentialException(String s, HttpStatus status) {
        super(s);
        this.status = status;
    }
}
