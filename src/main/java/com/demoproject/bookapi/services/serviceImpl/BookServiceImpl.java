package com.demoproject.bookapi.services.serviceImpl;

import com.demoproject.bookapi.dto.response.ApiResponse;
import com.demoproject.bookapi.dto.request.BookRequestDto;
import com.demoproject.bookapi.dto.response.BookResponseDto;
import com.demoproject.bookapi.entity.Book;
import com.demoproject.bookapi.entity.BorrowedBook;
import com.demoproject.bookapi.entity.Transaction;
import com.demoproject.bookapi.entity.User;
import com.demoproject.bookapi.enums.PaymentStatus;
import com.demoproject.bookapi.enums.Role;
import com.demoproject.bookapi.enums.TransactionType;
import com.demoproject.bookapi.exception.*;
import com.demoproject.bookapi.repository.BookRepository;
import com.demoproject.bookapi.repository.BorrowedBookRepository;
import com.demoproject.bookapi.repository.TransactionRepository;
import com.demoproject.bookapi.repository.UserRepository;
import com.demoproject.bookapi.services.BookService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
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
    private final TransactionRepository transactionRepository;

    @Override
    public ApiResponse<BookResponseDto> createBook(String email, BookRequestDto newBook) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getRole().equals(Role.AUTHOR) && newBook.getAuthorEmail() != null) {
            validateOtherAuthorsRole(newBook.getAuthorEmail());
        }
        validateBookAlreadyCreated(newBook.getTitle(), newBook.getAuthorEmail());
        Book createdBook = createNewBook(newBook, email);
        Book savedBook = bookRepository.save(createdBook);
        BookResponseDto response = createBookResponse(savedBook);
        return new ApiResponse<>("Book Created Successfully", response, HttpStatus.CREATED.value());
    }

    public void validateBookAlreadyCreated(String title, List<String> authorsEmail) {
        Optional<Book> bookCheck = bookRepository.findByTitleAndAuthorsEmail(title, authorsEmail);
        if (bookCheck.isPresent()) {
            throw new BookAlreadyCreatedException("Book already created with " + bookCheck.get().getTitle());
        }
    }

    public void validateOtherAuthorsRole(List<String> authorsEmail) {
        for (String authors : authorsEmail) {
            User user = userRepository.findByEmail(authors)
                    .orElseThrow(() -> new UserNotFoundException(("Author with email not found " + authors), HttpStatus.NOT_FOUND));
            if (user.getRole().equals(Role.USER)) {
                throw new UnAuthorizedAccessException("Access Denied, Cannot Create a Book");
            }
        }
    }

    private Book createNewBook(BookRequestDto newBook, String email) {
        return Book.builder()
                .title(newBook.getTitle())
                .authorsEmail(newBook.getAuthorEmail() == null ? Collections.singletonList(email) : newBook.getAuthorEmail())
                .isbn(newBook.getIsbn())
                .price(newBook.getPrice())
                .quantity(newBook.getQuantity())
                .build();
    }

    public BookResponseDto createBookResponse(Book savedBook) {
        return BookResponseDto.builder()
                .id(savedBook.getId())
                .title(savedBook.getTitle())
                .authorEmail(savedBook.getAuthorsEmail())
                .isbn(savedBook.getIsbn())
                .price(savedBook.getPrice())
                .quantity(savedBook.getQuantity())
                .build();
    }

    @Override
    public ApiResponse<BookResponseDto> editBook(Long id, BookRequestDto updatedBook) {
        Book retrievedBook = getBookById(id);
        retrievedBook.setTitle(updatedBook.getTitle());
        retrievedBook.setAuthorsEmail(updatedBook.getAuthorEmail());
        retrievedBook.setIsbn(updatedBook.getIsbn());
        retrievedBook.setPrice(updatedBook.getPrice());
        retrievedBook.setQuantity(updatedBook.getQuantity());

        validateBookAlreadyCreated(retrievedBook.getTitle(), retrievedBook.getAuthorsEmail());

        Book savedBook = bookRepository.save(retrievedBook);
        BookResponseDto response = createBookResponse(savedBook);
        return new ApiResponse<>("Book Edited Successfully", response, HttpStatus.OK.value());
    }

    @Override
    public ApiResponse<List<BookResponseDto>> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookResponseDto> responses = books.stream()
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
    public ApiResponse<BookResponseDto> borrowBook(Long bookId, Long userId) {
        Book book = getBookById(bookId);

        if (book.getQuantity() <= 0) {
            throw new BookNotAvailableException("Book is currently out of stock");
        }

        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser == null) {
            throw new UserNotFoundException("User Not Found", HttpStatus.NOT_FOUND);
        }

        BorrowedBook borrowedBook = createBorrowedBook(book, userId);
        borrowedBookRepository.save(borrowedBook);

        book.setQuantity(book.getQuantity() - 1);
        Book updatedBook = bookRepository.save(book);

        BookResponseDto response = createBookResponse(updatedBook);
        return new ApiResponse<>("Book Borrowed Successfully", response, HttpStatus.CREATED.value());
    }

    private BorrowedBook createBorrowedBook(Book requestedBook, Long userId) {
        Optional<User> existingUser = userRepository.findById(userId);

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            BigDecimal chargeAmount = calculateCharge(requestedBook);

            Transaction transaction = transactionRepository.save(Transaction.builder()
                    .user(user)
                    .amount(chargeAmount)
                    .type(TransactionType.BOOK_BORROW)
                    .build());

            if (chargeAmount.compareTo(BigDecimal.ZERO) > 0 && user.getBalance().compareTo(chargeAmount) > 0) {
                user.setBalance(user.getBalance().subtract(chargeAmount));
                userRepository.save(user);
                processPayment(transaction);
            } else {
                throw new InsufficientBalanceException("Insufficient Balance");
            }

            return BorrowedBook.builder()
                    .book(requestedBook)
                    .user(user)
                    .borrowedDate(LocalDate.now())
                    .transaction(transaction)
                    .build();
        } else {
            throw new UserNotFoundException("User Not Found", HttpStatus.NOT_FOUND);
        }
    }

    private BigDecimal calculateCharge(Book book) {
        return book.getPrice().multiply(BigDecimal.valueOf(0.1));
    }

    @Override
    public ApiResponse<String> returnBook(Long borrowedBookId) {
        BorrowedBook borrowedBook = borrowedBookRepository.findById(borrowedBookId)
                .orElseThrow(() -> new BookNotAvailableException("Borrowed book not found"));
        Book book = borrowedBook.getBook();
        Transaction transaction = borrowedBook.getTransaction();
        if (borrowedBook.getExpectedReturnDate().isAfter(LocalDate.now())) {
            transaction.setAmount(applyLateFees());
            transaction.setStatus(PaymentStatus.PENDING);
            processPayment(transaction);
        }

        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);
        borrowedBookRepository.delete(borrowedBook);

        return new ApiResponse<>("Book Returned Successfully", "Success", HttpStatus.NO_CONTENT.value());
    }

    private BigDecimal applyLateFees() {
        return BigDecimal.valueOf(500);
    }

    private void processPayment(Transaction transaction) {
        transaction.setStatus(PaymentStatus.PAID);
        transactionRepository.save(transaction);
    }

}
