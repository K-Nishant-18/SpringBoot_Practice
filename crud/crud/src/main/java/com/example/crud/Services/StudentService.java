package com.example.crud.Services;

import com.example.crud.Entity.Student;
import com.example.crud.Repository.SrudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final SrudentRepository repo;

    public StudentService(SrudentRepository repo){
        this.repo=repo;
    }

    @Transactional
    public Student create(Student student){
        return repo.save(student);
    }

    public List<Student> getAll(){
        return repo.findAll();
    }
}
