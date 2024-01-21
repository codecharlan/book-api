package com.demoproject.bookapi.dto.request;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BookRequestDto implements Serializable {
    private String title;
    private List<String> authorEmail;
    private String isbn;
    private BigDecimal price;
    private int quantity;
}
