package com.demoproject.bookapi.services.serviceImpl;

import com.demoproject.bookapi.dto.response.ApiResponse;
import com.demoproject.bookapi.dto.request.BookRequest;
import com.demoproject.bookapi.dto.response.BookResponse;
import com.demoproject.bookapi.entity.Book;
import com.demoproject.bookapi.entity.BorrowedBook;
import com.demoproject.bookapi.entity.User;
import com.demoproject.bookapi.exception.BookAlreadyCreatedException;
import com.demoproject.bookapi.exception.BookCannotBeDeletedException;
import com.demoproject.bookapi.exception.BookNotAvailableException;
import com.demoproject.bookapi.exception.UserNotFoundException;
import com.demoproject.bookapi.repository.BookRepository;
import com.demoproject.bookapi.repository.BorrowedBookRepository;
import com.demoproject.bookapi.repository.UserRepository;
import com.demoproject.bookapi.services.BookService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Setter
@Getter
@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BorrowedBookRepository borrowedBookRepository;
    private final UserRepository userRepository;
    @Override
    public ApiResponse<BookResponse> createBook(BookRequest newBook) {
        validateBookAlreadyCreated(newBook.getTitle());
        Book createdBook = createNewBook(newBook);
        Book savedBook = bookRepository.save(createdBook);
        BookResponse response = createBookResponse(savedBook);
        return new ApiResponse<>("Book Created Successfully", response, HttpStatus.CREATED.value());
    }
    public void validateBookAlreadyCreated(String title){
        Optional<Book> bookCheck = bookRepository.findByTitle(title);
        if (bookCheck.isPresent()) {
            throw new BookAlreadyCreatedException("Book already created with " + bookCheck.get().getTitle());
        }
    }
    private Book createNewBook(BookRequest newBook) {
        return Book.builder()
                .title(newBook.getTitle())
                .author(newBook.getAuthor())
                .isbn(newBook.getIsbn())
                .quantity(newBook.getQuantity())
                .build();
    }
    public BookResponse createBookResponse(Book savedBook){
        BookResponse bookResponse = new BookResponse();
        BeanUtils.copyProperties(savedBook, bookResponse);
        return bookResponse;
    }

    @Override
    public ApiResponse<BookResponse> editBook(Long id, Book updatedBook) {
        Book retrivedBook = getBookById(id);
        retrivedBook.setTitle(updatedBook.getTitle());
        retrivedBook.setAuthor(updatedBook.getAuthor());
        retrivedBook.setIsbn(updatedBook.getIsbn());
        retrivedBook.setQuantity(updatedBook.getQuantity());

        validateBookAlreadyCreated(retrivedBook.getTitle());

        Book savedBook = bookRepository.save(retrivedBook);
        BookResponse response = createBookResponse(savedBook);
        return new ApiResponse<>("Book Edited Successfully", response, HttpStatus.OK.value());
    }

    @Override
    public ApiResponse<List<BookResponse>> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookResponse> responses = books.stream()
                .map(this::createBookResponse)
                .collect(Collectors.toList());
        return new ApiResponse<>("Books Fetched Successfully", responses, HttpStatus.OK.value());
    }


    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotAvailableException("Book not found"));
    }
    @Override
    public ApiResponse<String> deleteBook(Long id) {
        Book book = getBookById(id);

        if (!book.getBorrowedBooks().isEmpty()) {
            throw new BookCannotBeDeletedException("Book cannot be deleted as it has borrowed copies.");
        }

        bookRepository.delete(book);
        return new ApiResponse<>("Book Deleted Successfully", "Deleted", HttpStatus.NO_CONTENT.value());
    }
    @Override
    public ApiResponse<BookResponse> borrowBook(Long bookId, Long userId) {
        Book book = getBookById(bookId);

        if (book.getQuantity() <= 0) {
            throw new BookNotAvailableException("Book is currently out of stock");
        }

        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser == null) {
            throw new UserNotFoundException("User not found");
        }

        BorrowedBook borrowedBook = createBorrowedBook(book, userId);
        borrowedBookRepository.save(borrowedBook);

        book.setQuantity(book.getQuantity() - 1);
        Book updatedBook = bookRepository.save(book);

        BookResponse response = createBookResponse(updatedBook);
        return new ApiResponse<>("Book Borrowed Successfully", response, HttpStatus.CREATED.value());
    }
    private BorrowedBook createBorrowedBook(Book requestedBook, Long userId) {
        Optional<User> existingUser = userRepository.findById(userId);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            return BorrowedBook.builder()
                    .book(requestedBook)
                    .user(user)
                    .borrowedDate(LocalDate.now())
                    .build();
        } else {
            throw new UserNotFoundException("User not found");
        }
    }
    @Override
    public ApiResponse<String> returnBook(Long borrowedBookId) {
        BorrowedBook borrowedBook = borrowedBookRepository.findById(borrowedBookId)
                .orElseThrow(() -> new BookNotAvailableException("Borrowed book, Not Found"));

        Book book = borrowedBook.getBook();
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);

        borrowedBookRepository.delete(borrowedBook);
        return new ApiResponse<>("Book Returned Successfully", "Success", HttpStatus.NO_CONTENT.value());
    }
}
