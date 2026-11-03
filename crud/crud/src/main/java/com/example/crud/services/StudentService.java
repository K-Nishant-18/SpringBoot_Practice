package com.example.crud.services;

import com.example.crud.dto.CreateStudentRequest;
import com.example.crud.dto.PatchStudentRequest;
import com.example.crud.dto.StudentResponse;
import com.example.crud.dto.UpdateStudentRequest;
import com.example.crud.entity.Student;
import com.example.crud.exceptions.StudentNotFoundException;
import com.example.crud.repository.StudentRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repo;

    public StudentService(StudentRepository repo){
        this.repo=repo;
    }

//    @Transactional
//    public Student create(Student student){
//        return repo.save(student);
//    }
    @Transactional
    public StudentResponse create(CreateStudentRequest request){
    Student student = new Student(request.name(), request.email(), request.age());
    Student saved = repo.save(student);
    return  StudentResponse.from(saved);
}

//    @Transactional(readOnly = true)
//    public List<Student> getAll(){
//        return repo.findAll();
//    }
    @Transactional(readOnly = true)
    public List<StudentResponse> getAll(){
        return repo.findAll()
                .stream()
                .map(StudentResponse::from)
                .toList();
    }

//    @Transactional(readOnly = true)
//    public Student getById(Long id){
//        return  repo.findById(id)
//                .orElseThrow(()->new StudentNotFoundException(id));
//    }

    @Transactional(readOnly = true)
    public StudentResponse getById(Long id){
        return StudentResponse.from(findEntity(id));
    }

//    @Transactional
//    public  Student update(Long id, Student incoming){
//        Student existing = getById(id);
//        existing.setName(incoming.getName());
//        existing.setAge(incoming.getAge());
//        existing.setEmail(incoming.getEmail());
//
//        return existing;
//    }
    @Transactional
    public StudentResponse update(Long id, UpdateStudentRequest request){
        Student existing = findEntity(id);
        existing.setName(request.name());
        existing.setEmail(request.email());
        existing.setAge(request.age());
        return StudentResponse.from(existing);
    }

//    @Transactional
//    public Student patch(Long id, Student incomming){
//        Student existing = getById(id);
//
//        if(incomming.getName() != null){
//            existing.setName(incomming.getName());
//        }
//        if(incomming.getEmail() != null){
//            existing.setEmail(incomming.getEmail());
//        }
//        if(incomming.getAge() != null){
//            existing.setAge(incomming.getAge());
//        }
//
//        return existing;
//    }

    @Transactional
    public StudentResponse patch(Long id, PatchStudentRequest request){
        Student existing = findEntity(id);

        if(request.name() != null){
            existing.setName(request.name());
        }
        if(request.age() != null){
            existing.setAge(request.age());
        }
        if(request.email() != null){
            existing.setEmail(request.email());
        }
        return StudentResponse.from(existing);
    }


//    @Transactional
//    public void delete(Long id){
//        Student existing = getById(id);
//        repo.delete(existing);
//    }

    @Transactional
    public void  delete(Long id){
        repo.delete(findEntity(id));
    }

    private Student findEntity(Long id){
        return  repo.findById(id)
                .orElseThrow(()-> new StudentNotFoundException(id));
    }
}
