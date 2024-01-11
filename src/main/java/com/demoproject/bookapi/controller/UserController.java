package com.demoproject.bookapi.controller;

import com.demoproject.bookapi.dto.request.LoginRequestDto;
import com.demoproject.bookapi.dto.request.RegistrationRequestDto;
import com.demoproject.bookapi.dto.response.ApiResponse;
import com.demoproject.bookapi.dto.response.LoginResponseDto;
import com.demoproject.bookapi.dto.response.RegistrationResponseDto;
import com.demoproject.bookapi.entity.User;
import com.demoproject.bookapi.security.JwtService;
import com.demoproject.bookapi.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegistrationResponseDto>> createUser(@Valid @RequestBody RegistrationRequestDto newUser) {
        ApiResponse<RegistrationResponseDto> response = userService.registerUser(newUser);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequest) {
        ApiResponse<LoginResponseDto> response = userService.login(loginRequest);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader(name = "Authorization") String authorizationHeader, HttpServletRequest request) {
        ApiResponse<String> response = userService.logout(request, authorizationHeader);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }
}
