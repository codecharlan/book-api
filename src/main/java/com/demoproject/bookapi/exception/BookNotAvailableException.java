package com.demoproject.bookapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class BookNotAvailableException extends RuntimeException {
    private final HttpStatus status;
    public BookNotAvailableException(String s) {
        super(s);
        this.status = HttpStatus.NOT_ACCEPTABLE;
    }
}
