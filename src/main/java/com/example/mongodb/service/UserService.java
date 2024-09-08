package com.example.mongodb.service;

import com.example.mongodb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    // Batch insert multiple users
    public List<User> saveAllUsers(List<User> users) {
        return (List<User>) mongoTemplate.insertAll(users);
    }

    public void deleteAllUsers(){
        mongoTemplate.remove(new Query(), User.class);
    }
}