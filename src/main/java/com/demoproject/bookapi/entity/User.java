package com.demoproject.bookapi.entity;

import com.demoproject.bookapi.enums.Gender;
import com.demoproject.bookapi.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String fullName;
    @Column(length = 100, nullable = false)
    @Email(message = "Must match a proper email")
    private String email;
    @Column(length = 100, nullable = false)
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDateTime lastLogin;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Book> book = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<BorrowedBook> borrowedBooks = new ArrayList<>();
}
