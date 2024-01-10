package com.demoproject.bookapi.services;

import com.demoproject.bookapi.dto.request.BookRequest;
import com.demoproject.bookapi.dto.response.ApiResponse;
import com.demoproject.bookapi.dto.response.BookResponse;
import com.demoproject.bookapi.entity.Book;

import java.util.List;

public interface BookService {
    ApiResponse<BookResponse> createBook(BookRequest newBook);

    ApiResponse<BookResponse> editBook(Long id, Book updatedBook);

    ApiResponse<List<BookResponse>> getAllBooks();

    ApiResponse<String> deleteBook(Long id);
    ApiResponse<BookResponse> borrowBook(Long bookId, Long userId);

    ApiResponse<String> returnBook(Long borrowedBookId);
}
