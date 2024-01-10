package com.demoproject.bookapi.controller;

import com.demoproject.bookapi.dto.request.UserRequest;
import com.demoproject.bookapi.dto.response.ApiResponse;
import com.demoproject.bookapi.dto.response.UserResponse;
import com.demoproject.bookapi.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest newUser) {
        ApiResponse<UserResponse> response = userService.createUser(newUser);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }
}
