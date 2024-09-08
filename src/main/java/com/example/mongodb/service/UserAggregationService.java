package com.example.mongodb.service;

import com.example.mongodb.model.User;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class UserAggregationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUsersByAgeRange(int minAge, int maxAge) {
        Aggregation aggregation = Aggregation.newAggregation(
                match(Criteria.where("age").gte(minAge).lte(maxAge)),
                sort(Sort.by(Sort.Direction.DESC, "age"))
        );

        AggregationResults<User> results = mongoTemplate.aggregate(aggregation, "users", User.class);
        return results.getMappedResults();
    }

    public List<UserAggregationResult> getAgeGroupStatistics() {
        Aggregation aggregation = Aggregation.newAggregation(
                group("age").count().as("count"),
                project("count").and("age").previousOperation()
        );

        AggregationResults<UserAggregationResult> results = mongoTemplate.aggregate(aggregation, "users", UserAggregationResult.class);
        return results.getMappedResults();
    }

    @Data
    @ToString
    public static class UserAggregationResult {
        private int age;
        private int count;
    }
}
