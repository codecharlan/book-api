package com.demoproject.bookapi.dto.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String jwtToken;
}
