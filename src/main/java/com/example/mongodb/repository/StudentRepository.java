package com.example.mongodb.repository;

import com.example.mongodb.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {
Optional<Student> findStudentByEmail(String email);
}
