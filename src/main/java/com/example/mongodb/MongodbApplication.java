package com.example.mongodb;

import com.example.mongodb.model.Address;
import com.example.mongodb.model.Gender;
import com.example.mongodb.model.Student;
import com.example.mongodb.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class MongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongodbApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(StudentRepository studentRepository, MongoTemplate mongoTemplate) {
        return args -> {
            String email = "ali.alavi@gmail.com";
            Address address = new Address("Iran", "Tehran", "+98");
            Student student = new Student("Ali", "Alavi", email, Gender.MALE, address, List.of("Computer science"), BigDecimal.TEN, LocalDateTime.now());

            //usingMongoTemplateAndQuery(studentRepository, mongoTemplate, email, student);

            studentRepository.findStudentByEmail(email).ifPresentOrElse(s -> {
                System.out.println(s + " already exists");
            }, () -> {
                System.out.println("Inserting student " + student);
                studentRepository.insert(student);
            });
        };
    }

    private static void usingMongoTemplateAndQuery(StudentRepository studentRepository, MongoTemplate mongoTemplate, String email, Student student) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        List<Student> students = mongoTemplate.find(query, Student.class);

        if (students.size() > 1) {
            throw new IllegalStateException("found many students with email " + email);
        }

        if (students.isEmpty()) {
            System.out.println("Inserting student " + student);
            studentRepository.insert(student);
        }
    }
}