package com.demoproject.bookapi.exception;

public class BookAlreadyCreatedException extends RuntimeException {
    public BookAlreadyCreatedException(String s) {
        super(s);
    }
}
