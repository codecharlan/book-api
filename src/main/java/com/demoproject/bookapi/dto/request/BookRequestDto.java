package com.demoproject.bookapi.dto.request;

import com.demoproject.bookapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequestDto {
    private String title;
    private List<String> authorEmail;
    private String isbn;
    private int quantity;
}
