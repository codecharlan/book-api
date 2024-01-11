package com.demoproject.bookapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnAuthorizedException extends RuntimeException {
    private final HttpStatus status;
    public UnAuthorizedException(String s) {
        super(s);
        this.status = HttpStatus.UNAUTHORIZED;
    }
}
