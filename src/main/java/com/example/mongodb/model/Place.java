package com.example.mongodb.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "places")
public class Place {
    @Id
    private String id;
    private String name;

    @GeoSpatialIndexed
    private double[] location;  // [longitude, latitude]
}

