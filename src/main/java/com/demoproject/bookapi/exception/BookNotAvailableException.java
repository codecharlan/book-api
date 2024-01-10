package com.demoproject.bookapi.exception;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(String s) {
        super(s);
    }
}
