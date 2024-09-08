package com.example.mongodb.controller;

import com.example.mongodb.model.Place;
import com.example.mongodb.service.GeoSpatialService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/places")
public class PlaceController {

    private final GeoSpatialService geoSpatialService;

    @GetMapping("/near")
    public List<Place> findPlacesNear(
            @RequestParam double longitude,
            @RequestParam double latitude,
            @RequestParam double maxDistanceKm) {
        return geoSpatialService.findPlacesNear(longitude, latitude, maxDistanceKm);
    }
}

