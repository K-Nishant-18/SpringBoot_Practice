package com.example.crud.Services;

import com.example.crud.Entity.Student;
import com.example.crud.Exceptions.StudentNotFoundException;
import com.example.crud.Repository.StudentRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repo;

    public StudentService(StudentRepository repo){
        this.repo=repo;
    }

    @Transactional
    public Student create(Student student){
        return repo.save(student);
    }

    @Transactional(readOnly = true)
    public List<Student> getAll(){
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Student getById(Long id){
        return  repo.findById(id)
                .orElseThrow(()->new StudentNotFoundException(id));
    }

    @Transactional
    public  Student update(Long id, Student incoming){
        Student existing = getById(id);
        existing.setName(incoming.getName());
        existing.setAge(incoming.getAge());
        existing.setEmail(incoming.getEmail());

        return existing;
    }

    public Student patch(Long id, Student incomming){
        Student existing = getById(id);

        if(incomming.getName() != null){
            existing.setName(incomming.getName());
        }
        if(incomming.getEmail() != null){
            existing.setEmail(incomming.getEmail());
        }
        if(incomming.getAge() != null){
            existing.setAge(incomming.getAge());
        }

        return existing;
    }


    @Transactional
    public void delete(Long id){
        Student existing = getById(id);
        repo.delete(existing);
    }
}
