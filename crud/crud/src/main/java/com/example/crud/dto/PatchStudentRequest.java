package com.example.crud.dto;

import jakarta.validation.constraints.*;

public record PatchStudentRequest (

        @Pattern(regexp = ".*\\S.*", message = "name must not be blank")
        @Size(max = 100, message = "name must be at most 100 characters")
        String name,

        @Pattern(regexp = ".*\\S.*", message = "email must not be blank")
        @Email(message = "email must be a valid email address")
        @Size(max = 100, message = "email must be at most 100 characters")
        String email,

        @Min(value = 1, message = "age must be at least 1")
        @Max(value = 120, message = "age must be at most 120")
        Integer age
) {
}
