package com.demoproject.bookapi.route;

import com.demoproject.bookapi.dto.request.BookRequestDto;
import com.demoproject.bookapi.security.JwtService;
import com.demoproject.bookapi.services.serviceImpl.BookServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.demoproject.bookapi.constants.Constant.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class BookApiRoute extends RouteBuilder {
    private final JwtService jwtService;

    @Override
    public void configure() throws Exception {

        rest("/books")
                .get("/{id}")
                .consumes(APPLICATION_JSON_VALUE)
                .description("Get Books By ID")
                .to(DIRECT_GET_BOOK)

                .get("/allbooks")
                .description("Get All Books")
                .produces(APPLICATION_JSON_VALUE)
                .to(DIRECT_GET_ALL_BOOK)

                .post("/create")
                .to(DIRECT_CREATE_BOOK)
                .consumes(APPLICATION_JSON_VALUE)
                .description("Create New Book")

                .put("/edit/{id}")
                .consumes(APPLICATION_JSON_VALUE)
                .description("Edit Existing Books By ID")
                .to(DIRECT_EDIT_BOOK)

                .delete("/delete/{id}")
                .consumes(APPLICATION_JSON_VALUE)
                .description("Delete Existing Books By ID")
                .to(DIRECT_DELETE_BOOK)

                .post("/borrow")
                .consumes(APPLICATION_JSON_VALUE)
                .description("Borrow Books")
                .to(DIRECT_BORROW_BOOK)

                .post("/return")
                .consumes(APPLICATION_JSON_VALUE)
                .description("Return Borrowed Books")
                .to(DIRECT_RETURN_BOOK);

        from(DIRECT_CREATE_BOOK)
                .log(LoggingLevel.INFO, "Before Marshaling Request: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, BookRequestDto.class)
                .log(LoggingLevel.ERROR, "Book Creation Request ${body}")
                .process(this::extractEmailFromJwtAndAddToHeader)
                .process(exchange -> exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, CREATED.value()))
                .bean(BookServiceImpl.class, "createBook(${header.email}, ${body})")
                .marshal().json();

        from(DIRECT_GET_BOOK)
                .bean(BookServiceImpl.class, "getBookById(${header.id})")
                .marshal().json();

        from(DIRECT_EDIT_BOOK)
                .unmarshal().json(JsonLibrary.Jackson, BookRequestDto.class)
                .process(exchange -> exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, OK.value()))
                .bean(BookServiceImpl.class, "editBook(${header.id}, ${body})")
                .marshal().json();

        from(DIRECT_GET_ALL_BOOK)
                .bean(BookServiceImpl.class, "getAllBooks")
                .marshal().json();

        from(DIRECT_DELETE_BOOK)
                .bean(BookServiceImpl.class, "deleteBook(${header.id})")
                .marshal().json();

        from(DIRECT_BORROW_BOOK)
                .bean(BookServiceImpl.class, "borrowBook(${header.id}, ${header.userId})")
                .marshal().json();

        from(DIRECT_RETURN_BOOK)
                .bean(BookServiceImpl.class, "returnBook(${header.id})")
                .marshal().json();

    }

    private void extractEmailFromJwtAndAddToHeader(Exchange exchange) {
        String authorizationHeader = (String) exchange.getMessage().getHeader("Authorization");
        System.out.println(authorizationHeader);
        Map<String, String> userDetails = jwtService.validateTokenAndReturnDetail(authorizationHeader.substring(7));
        String primaryAuthorEmail = userDetails.get("email");
        exchange.getMessage().setHeader("email", primaryAuthorEmail);
    }
}
