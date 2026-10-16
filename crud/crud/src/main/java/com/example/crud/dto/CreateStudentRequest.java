package com.example.crud.dto;

import jakarta.validation.constraints.*;

public record CreateStudentRequest (

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be within 100 characters ")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid email")
        @Size(message = "Email must be within 100 characters")
        String email,

        @Min(value = 1, message = "Age must be at least 1")
        @Max(value = 120, message = "Age must be at most 120")
        Integer age

){

}
