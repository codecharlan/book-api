package com.demoproject.bookapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnAuthorizedAccessException extends RuntimeException {
    private final HttpStatus status;
    public UnAuthorizedAccessException(String s) {
        super(s);
        this.status = HttpStatus.UNAUTHORIZED;
    }
}
