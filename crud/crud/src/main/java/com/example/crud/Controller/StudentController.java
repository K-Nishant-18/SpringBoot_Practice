package com.example.crud.Controller;


import com.example.crud.Entity.Student;
import com.example.crud.Services.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

@Controller
@RequestMapping("/api/students")
public class StudentController {

    private  final StudentService studentService;

    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student){
        Student saved = studentService.create(student);
        URI location = URI.create("/api/students/" + saved.getId());
        return  ResponseEntity.created(location).body(saved);
    }

}
