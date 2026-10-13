package com.example.crud.Repository;

import com.example.crud.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SrudentRepository extends JpaRepository<Student, Long> {
}
