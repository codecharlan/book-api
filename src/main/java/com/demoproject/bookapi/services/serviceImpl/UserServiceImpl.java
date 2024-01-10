package com.demoproject.bookapi.services.serviceImpl;

import com.demoproject.bookapi.dto.request.UserRequest;
import com.demoproject.bookapi.dto.response.ApiResponse;
import com.demoproject.bookapi.dto.response.UserResponse;
import com.demoproject.bookapi.entity.User;
import com.demoproject.bookapi.repository.UserRepository;
import com.demoproject.bookapi.services.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Setter
@Getter
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public ApiResponse<UserResponse> createUser(UserRequest newUser) {
        User createdUser = createNewUser(newUser);
        User savedBook = userRepository.save(createdUser);
        UserResponse response = createUserResponse(savedBook);
        return new ApiResponse<>("User Created Successfully", response, HttpStatus.CREATED.value());
    }

    private User createNewUser(UserRequest newUser) {
        return User.builder()
                .fullName(newUser.getFullName())
                .email(newUser.getEmail())
                .password(newUser.getPassword())
                .build();
    }

    public UserResponse createUserResponse(User savedUser) {
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(savedUser, userResponse);
        return userResponse;
    }
}
