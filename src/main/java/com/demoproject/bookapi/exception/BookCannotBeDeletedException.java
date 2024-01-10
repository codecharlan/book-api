package com.demoproject.bookapi.exception;

public class BookCannotBeDeletedException extends RuntimeException {
    public BookCannotBeDeletedException(String s) {
        super(s);
    }
}
