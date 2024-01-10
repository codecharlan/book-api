package com.demoproject.bookapi.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(length = 100, nullable = false)
    private String author;
    @Column(length = 100, nullable = false)
    private String isbn;
    @Column(nullable = false)
    private int quantity;
    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    private List<BorrowedBook> borrowedBooks = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
