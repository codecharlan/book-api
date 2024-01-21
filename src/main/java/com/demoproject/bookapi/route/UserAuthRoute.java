package com.demoproject.bookapi.route;

import com.demoproject.bookapi.dto.request.LoginRequestDto;
import com.demoproject.bookapi.dto.request.RegistrationRequestDto;
import com.demoproject.bookapi.services.serviceImpl.UserServiceImpl;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Component
public class UserAuthRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        rest("/users")
                .post("/register")
                .consumes(APPLICATION_JSON_VALUE)
                .produces(APPLICATION_JSON_VALUE)
                .description("Register New User")
                .to("direct:register-user")

                .post("/login")
                .consumes(APPLICATION_JSON_VALUE)
                .produces(APPLICATION_JSON_VALUE)
                .description("Login Existing User")
                .to("direct:login")

                .post("/logout")
                .consumes(APPLICATION_JSON_VALUE)
                .produces(APPLICATION_JSON_VALUE)
                .description("Log User Out of Session")
                .to("direct:logout");

        from("direct:register-user")
                .log(LoggingLevel.INFO, "Received Registration Request: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, RegistrationRequestDto.class)
                .bean(UserServiceImpl.class, "registerUser")
                .process(exchange -> exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.CREATED.value()))
                .marshal().json(JsonLibrary.Jackson);

        from("direct:login")
                .log(LoggingLevel.INFO, "Received Login Request: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, LoginRequestDto.class)
                .bean(UserServiceImpl.class, "login")
                .process(exchange -> exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value()))
                .marshal().json(JsonLibrary.Jackson);

        from("direct:logout")
                .log(LoggingLevel.INFO, "Received LogOut Request: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, String.class)
                .bean(UserServiceImpl.class, "logout")
                .marshal().json(JsonLibrary.Jackson);
    }
}
