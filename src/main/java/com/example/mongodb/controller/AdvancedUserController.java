package com.example.mongodb.controller;

import com.example.mongodb.model.User;
import com.example.mongodb.service.UserService;
import com.example.mongodb.service.UserAggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/advanced-users")
@RequiredArgsConstructor
public class AdvancedUserController {

    private final UserService userService;
    private final UserAggregationService userAggregationService;

    @PostMapping("/batch")
    public List<User> batchInsertUsers(@RequestBody List<User> users) {
        return userService.saveAllUsers(users);
    }

    @GetMapping("/age-range")
    public List<User> getUsersByAgeRange(@RequestParam int minAge, @RequestParam int maxAge) {
        return userAggregationService.getUsersByAgeRange(minAge, maxAge);
    }

    @GetMapping("/age-statistics")
    public List<UserAggregationService.UserAggregationResult> getAgeGroupStatistics() {
        return userAggregationService.getAgeGroupStatistics();
    }
}