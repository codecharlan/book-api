package com.demoproject.bookapi.services;

import com.demoproject.bookapi.dto.request.UserRequest;
import com.demoproject.bookapi.dto.response.ApiResponse;
import com.demoproject.bookapi.dto.response.UserResponse;

public interface UserService {
    ApiResponse<UserResponse> createUser(UserRequest newUser);
}
