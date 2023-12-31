package com.example.mongodb.controller;

import com.example.mongodb.model.Student;
import com.example.mongodb.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/students")
@AllArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/all")
    public List<Student> fetchAllStudents() {
        return studentService.getAllStudents();
    }
}