package com.demoproject.bookapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false, unique = true)
    private String title;
    @Column(length = 100)
    private List<String> authorsEmail = new ArrayList<>();
    @Column(length = 100, nullable = false)
    private String isbn;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private BigDecimal price;
    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    private List<BorrowedBook> borrowedBooks = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}