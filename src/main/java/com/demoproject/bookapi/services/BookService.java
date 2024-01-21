package com.demoproject.bookapi.services;

import com.demoproject.bookapi.dto.request.BookRequestDto;
import com.demoproject.bookapi.dto.response.ApiResponse;
import com.demoproject.bookapi.dto.response.BookResponseDto;
import com.demoproject.bookapi.entity.Book;
import com.demoproject.bookapi.entity.User;

import java.util.List;

public interface BookService {
    ApiResponse<BookResponseDto> createBook(String email, BookRequestDto newBook);

    ApiResponse<BookResponseDto> editBook(Long id, BookRequestDto updatedBook);

    ApiResponse<List<BookResponseDto>> getAllBooks();

    ApiResponse<String> deleteBook(Long id);
    ApiResponse<BookResponseDto> borrowBook(Long bookId, Long userId);

    ApiResponse<String> returnBook(Long borrowedBookId);
}
