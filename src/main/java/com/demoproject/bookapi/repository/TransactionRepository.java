package com.demoproject.bookapi.repository;

import com.demoproject.bookapi.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long > {
}
