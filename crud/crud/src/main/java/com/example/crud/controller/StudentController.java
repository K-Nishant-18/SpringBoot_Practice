package com.example.crud.controller;


import com.example.crud.entity.Student;
import com.example.crud.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(
        name = "Student Management",
        description = "APIs for managing student records"
)
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private  final StudentService studentService;

    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }


    @Operation(
            summary = "Create a new student",
            description = "Creates a student record and returns the saved student."
    )
    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student){
        Student saved = studentService.create(student);
        URI location = URI.create("/api/students/" + saved.getId());
        return  ResponseEntity.created(location).body(saved);
    }



    @Operation(
            summary = "Get all students",
            description = "Retrieves all student records from the database."
    )
    @GetMapping
    public List<Student> getAll(){
        return studentService.getAll();
    }



    @Operation(
            summary = "Get a student by ID",
            description = "Retrieves a single student using their unique ID."
    )
    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id){
        return studentService.getById(id);
    }



    @Operation(
            summary = "Update a student",
            description = "Updates the student's name, email and age."
    )
    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody Student student){
        return studentService.update(id, student);
    }



    @Operation(
            summary = "Partially update a student",
            description = "Updates only the non-null fields supplied in the request."
    )
    @PatchMapping("/{id}")
    public Student patch(@PathVariable Long id, @RequestBody Student student){
        return studentService.patch(id, student);
    }



    @Operation(
            summary = "Delete a student",
            description = "Deletes a student record using its unique ID."
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        studentService.delete(id);
    }


}
