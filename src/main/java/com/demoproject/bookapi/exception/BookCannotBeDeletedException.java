package com.demoproject.bookapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class BookCannotBeDeletedException extends RuntimeException {
    private final HttpStatus status;
    public BookCannotBeDeletedException(String s) {
        super(s);
        this.status = HttpStatus.BAD_REQUEST;
    }
}
