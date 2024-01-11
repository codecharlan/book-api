package com.demoproject.bookapi.dto.response;

import com.demoproject.bookapi.enums.Gender;
import com.demoproject.bookapi.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationResponseDto {
    private String fullName;
    private String email;
    private Gender gender;
    private Role role;
}
