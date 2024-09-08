package com.example.mongodb.repository;

import com.example.mongodb.model.Place;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlaceRepository extends MongoRepository<Place, String> {
    List<Place> findByLocationNear(Point location, Distance distance);
}