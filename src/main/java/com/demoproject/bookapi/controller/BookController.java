package com.demoproject.bookapi.controller;

import com.demoproject.bookapi.dto.request.BookRequest;
import com.demoproject.bookapi.dto.response.ApiResponse;
import com.demoproject.bookapi.dto.response.BookResponse;
import com.demoproject.bookapi.entity.Book;
import com.demoproject.bookapi.services.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<BookResponse>> createBook(@Valid @RequestBody BookRequest newBook) {
        ApiResponse<BookResponse> response = bookService.createBook(newBook);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> editBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        ApiResponse<BookResponse> response = bookService.editBook(id, updatedBook);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }
    @GetMapping("/all-books")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getAllBooks() {
        ApiResponse<List<BookResponse>> response = bookService.getAllBooks();
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable Long id) {
        ApiResponse<String> response = bookService.deleteBook(id);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }

    @PostMapping("/borrow/{id}/{userId}")
    public ResponseEntity<ApiResponse<BookResponse>> borrowBook(@PathVariable Long id, @PathVariable Long userId) {
        ApiResponse<BookResponse> response = bookService.borrowBook(id, userId);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }

    @PostMapping("/borrowed/return/{id}")
    public ResponseEntity<ApiResponse<String>> returnBook(@PathVariable Long id) {
        ApiResponse<String> response = bookService.returnBook(id);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }
}
