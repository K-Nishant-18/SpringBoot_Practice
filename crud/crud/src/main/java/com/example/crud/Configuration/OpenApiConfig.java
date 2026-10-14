package com.example.crud.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Student Management API",
                version = "1.0.0",
                description = "A beginner-friendly REST API built with Spring Boot 3.2.4, Spring Data JPA and the H2 in-memory database. It performs full CRUD (Create, Read, Update, Delete) on students, plus PATCH for partial updates.",
                contact = @Contact(
                        name = "Kumar Nishant",
                        url = "https://github.com/K-Nishant-18",
                        email = "me.knishant@gmail.com"
                )
        )

)
public class OpenApiConfig {
}
