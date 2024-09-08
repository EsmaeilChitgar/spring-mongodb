package com.example.mongodb.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "users")
@ToString
public class User {
    @Id
    private String id;
    private String name;
    private int age;
    private String email;
}