package com.demoproject.bookapi.services;

import com.demoproject.bookapi.dto.request.LoginRequestDto;
import com.demoproject.bookapi.dto.request.RegistrationRequestDto;
import com.demoproject.bookapi.dto.response.ApiResponse;
import com.demoproject.bookapi.dto.response.LoginResponseDto;
import com.demoproject.bookapi.dto.response.RegistrationResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    ApiResponse<RegistrationResponseDto> registerUser(RegistrationRequestDto registrationRequest);

    ApiResponse<LoginResponseDto> login(LoginRequestDto loginRequest);

    ApiResponse<String> logout(HttpServletRequest request, String authorizationHeader);
}
