package com.example.mongodb.service;

import com.example.mongodb.model.Place;
import com.example.mongodb.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GeoSpatialService {

    private final PlaceRepository placeRepository;

    public List<Place> findPlacesNear(double longitude, double latitude, double maxDistanceKm) {
        Point location = new Point(longitude, latitude);
        Distance distance = new Distance(maxDistanceKm, Metrics.KILOMETERS);
        return placeRepository.findByLocationNear(location, distance);
    }
}