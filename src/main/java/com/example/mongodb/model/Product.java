package com.example.mongodb.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Map;

@Data
@Builder
@Document(collection = "products")
public class Product {

    @Id
    private String id;
    private String name;
    private double price;
    private Map<String, Object> additionalProperties;
}
