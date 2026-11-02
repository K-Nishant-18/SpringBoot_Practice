package com.example.crud.dto;

import com.example.crud.entity.Student;

public record StudentResponse(
        Long id,
        String name,
        String email,
        Integer age
) {
    public static StudentResponse from(Student student){
        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getAge()
        );
    }

}
