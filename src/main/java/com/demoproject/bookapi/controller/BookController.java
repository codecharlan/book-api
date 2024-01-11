package com.demoproject.bookapi.controller;

import com.demoproject.bookapi.dto.request.BookRequestDto;
import com.demoproject.bookapi.dto.response.ApiResponse;
import com.demoproject.bookapi.dto.response.BookResponseDto;
import com.demoproject.bookapi.entity.Book;
import com.demoproject.bookapi.security.JwtService;
import com.demoproject.bookapi.services.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<BookResponseDto>> createBook(@RequestHeader(name = "Authorization") String authorizationHeader, @Valid @RequestBody BookRequestDto newBook) {
        System.out.println(authorizationHeader);
        Map<String, String> userDetails = jwtService.validateTokenAndReturnDetail(authorizationHeader.substring(7));
        String primaryAuthorEmail = userDetails.get("email");
        ApiResponse<BookResponseDto> response = bookService.createBook(primaryAuthorEmail, newBook);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<ApiResponse<BookResponseDto>> editBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        ApiResponse<BookResponseDto> response = bookService.editBook(id, updatedBook);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }
    @GetMapping("/all-books")
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> getAllBooks() {
        ApiResponse<List<BookResponseDto>> response = bookService.getAllBooks();
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
    public ResponseEntity<ApiResponse<BookResponseDto>> borrowBook(@PathVariable Long id, @PathVariable Long userId) {
        ApiResponse<BookResponseDto> response = bookService.borrowBook(id, userId);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<ApiResponse<String>> returnBook(@PathVariable Long id) {
        ApiResponse<String> response = bookService.returnBook(id);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }
}
