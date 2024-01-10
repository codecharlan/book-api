package com.demoproject.bookapi.dto.response;

public record ApiResponse<T>(String message, T data, int status) {

}
