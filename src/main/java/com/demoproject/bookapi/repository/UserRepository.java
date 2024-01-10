package com.demoproject.bookapi.repository;

import com.demoproject.bookapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
