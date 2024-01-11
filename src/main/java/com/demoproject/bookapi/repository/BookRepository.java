package com.demoproject.bookapi.repository;

import com.demoproject.bookapi.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT DISTINCT b FROM Book b " +
            "WHERE b.title = :title AND b.authorsEmail IN :authorsEmail")
    Optional<Book> findByTitleAndAuthorsEmail(String title, List<String> authorsEmail);

}
