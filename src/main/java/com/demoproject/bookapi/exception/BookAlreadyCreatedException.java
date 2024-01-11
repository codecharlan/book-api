package com.demoproject.bookapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class BookAlreadyCreatedException extends RuntimeException {
    private final HttpStatus status;
    public BookAlreadyCreatedException(String s) {
        super(s);
        this.status = HttpStatus.CONFLICT;
    }
}
