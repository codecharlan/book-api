package com.demoproject.bookapi.dto.response;

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
public class BookResponse {
    private String title;
    private String author;
    private String isbn;
    private int quantity;
}
