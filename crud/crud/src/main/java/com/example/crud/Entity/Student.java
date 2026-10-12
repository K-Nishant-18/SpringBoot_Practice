package com.example.crud.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    private Integer age;

    protected Student(){
    }

    public Student(String name, String email, Integer age){
        this.name=name;
        this.email=email;
        this.age=age;
    }


}
