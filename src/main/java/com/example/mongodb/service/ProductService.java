package com.example.mongodb.service;

import com.example.mongodb.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final MongoTemplate mongoTemplate;

    public List<Product> findProductsByManufacturer(String manufacturer) {
        Query query = new Query();
        query.addCriteria(Criteria.where("additionalProperties.manufacturer").is(manufacturer));

        return mongoTemplate.find(query, Product.class);
    }

    public Product save(Product product){
        return mongoTemplate.insert(product);
    }

    public void deleteAll(){
        mongoTemplate.remove(new Query(), Product.class);
    }
}

