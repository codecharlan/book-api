package com.demoproject.bookapi.repository;

import com.demoproject.bookapi.entity.BorrowedBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {
}
