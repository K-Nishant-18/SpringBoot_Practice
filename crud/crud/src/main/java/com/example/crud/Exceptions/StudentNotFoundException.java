package com.example.crud.Exceptions;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id){
        super("Student" + id+ "nor found");
    }
}
