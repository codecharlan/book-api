package com.demoproject.bookapi.services.serviceImpl;

import com.demoproject.bookapi.dto.request.LoginRequestDto;
import com.demoproject.bookapi.dto.request.RegistrationRequestDto;
import com.demoproject.bookapi.dto.response.ApiResponse;
import com.demoproject.bookapi.dto.response.LoginResponseDto;
import com.demoproject.bookapi.dto.response.RegistrationResponseDto;
import com.demoproject.bookapi.entity.User;
import com.demoproject.bookapi.exception.InvalidCredentialException;
import com.demoproject.bookapi.exception.UserAlreadyExistException;
import com.demoproject.bookapi.exception.UserNotFoundException;
import com.demoproject.bookapi.repository.UserRepository;
import com.demoproject.bookapi.security.JwtService;
import com.demoproject.bookapi.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Setter
@Getter
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public ApiResponse<RegistrationResponseDto> registerUser(RegistrationRequestDto registrationRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(registrationRequest.getEmail());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistException("User Already Exists");
        }

        User newUser = createNewUser(registrationRequest);

        User savedUser = userRepository.save(newUser);

        RegistrationResponseDto registrationResponse = createUserResponse(savedUser);

        return new ApiResponse<>("User Created Successfully", registrationResponse, HttpStatus.CREATED.value());
    }

    private User createNewUser(RegistrationRequestDto newUser) {
        return User.builder()
                .fullName(newUser.getFullName())
                .email(newUser.getEmail())
                .gender(newUser.getGender())
                .role(newUser.getRole())
                .password(passwordEncoder.encode(newUser.getPassword()))
                .build();
    }
    private RegistrationResponseDto createUserResponse(User savedUser) {
        return RegistrationResponseDto.builder()
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .gender(savedUser.getGender())
                .role(savedUser.getRole())
                .build();
    }
    @Override
    public ApiResponse<LoginResponseDto> login(LoginRequestDto loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("User Not Found", HttpStatus.NOT_FOUND));

            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            String token = jwtService.generateToken(authentication, user.getRole());

            LoginResponseDto loginResponse = createLoginResponse(user, token);

            return new ApiResponse<>( "Successfully logged in", loginResponse, HttpStatus.OK.value());
        } catch (DisabledException e) {
            throw new RuntimeException("User is disabled");
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }
    private LoginResponseDto createLoginResponse(User savedUser, String token) {
        return LoginResponseDto.builder()
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .jwtToken(token)
                .build();
    }

    @Override
    public ApiResponse<String> logout(HttpServletRequest request, String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7);
            Map<String, String> userDetails = jwtService.validateTokenAndReturnDetail(token);
            if (userDetails != null) {
                SecurityContextHolder.clearContext();
                return new ApiResponse<>( "Successfully logged out", "You have been logged out", HttpStatus.OK.value());
            }
            return new ApiResponse<>("Already out of Session", "User not in Session", HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            return new ApiResponse<>( "Logout failed", "An error occurred while logging out", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
