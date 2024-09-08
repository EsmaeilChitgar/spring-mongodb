package com.example.mongodb;

import com.example.mongodb.model.*;
import com.example.mongodb.repository.PlaceRepository;
import com.example.mongodb.repository.ProductRepository;
import com.example.mongodb.repository.StudentRepository;
import com.example.mongodb.service.ProductService;
import com.example.mongodb.service.UserService;
import com.example.mongodb.service.UserAggregationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class MongodbApplication {

    private final MongoTemplate mongoTemplate;
    private final UserService userService;
    private final UserAggregationService userAggregationService;
    private final PlaceRepository placeRepository;
    private final StudentRepository studentRepository;
    private final ProductService productService;

    public static void main(String[] args) {
        SpringApplication.run(MongodbApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        return args -> {
            userTest();
            geoLocationTest();
            studentTest();
            productTest();
        };
    }

    private void userTest() {
        userService.deleteAllUsers();
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            users.add(User.builder().name("name" + i + 1).age(i + 10).email("name" + i + 1 + "@domain.com").build());
            users.add(User.builder().name("name" + 20 + (i + 1)).age(i + 10).email("name" + 20 + (i + 1) + "@domain.com").build());
        }
        userService.saveAllUsers(users);

        log.info("print getUsersByAgeRange -----------------------------------------");
        userAggregationService.getUsersByAgeRange(12, 16).forEach(user -> log.info(user.toString()));

        log.info("print getAgeGroupStatistics -----------------------------------------");
        userAggregationService.getAgeGroupStatistics().forEach(st -> log.info(st.toString()));
    }

    private void geoLocationTest() {
        placeRepository.deleteAll();

        log.info("Inserting new place");
        placeRepository.save(Place.builder().name("Central Park").location(new double[]{-73.968285, 40.785091}).build()); // New York
        placeRepository.save(Place.builder().name("Golden Gate Bridge").location(new double[]{-122.478255, 37.819929}).build()); // San Francisco
        placeRepository.save(Place.builder().name("Eiffel Tower").location(new double[]{2.294694, 48.858093}).build()); // Paris
        placeRepository.save(Place.builder().name("Taj Mahal").location(new double[]{78.042155, 27.175015}).build()); // India
        log.info("Places inserted successfully.");

        // Define a point near New York City
        Point currentLocation = new Point(-73.985428, 40.748817); // New York City (near Empire State Building)
        Distance maxDistance = new Distance(200, Metrics.KILOMETERS); // 200 KM search radius

        // Query for the nearest place within 200 KM
        List<Place> nearestPlaces = placeRepository.findByLocationNear(currentLocation, maxDistance);

        log.info("Nearest places within 200 KM:");
        nearestPlaces.forEach(place ->
                log.info("Place: " + place.getName() + " at Location: " + place.getLocation()[0] + ", " + place.getLocation()[1])
        );
    }

    private void studentTest() {
        studentRepository.deleteAll();
        studentTestUsingMongoTemplateAndQuery();
        studentTestUsingStudentRepository();
    }

    private void studentTestUsingStudentRepository() {
        String email = "ali.saeedi@gmail.com";
        Address address = new Address("Iran", "Tehran", "+98");
        Student student = new Student("Ali", "Saeedi", email, Gender.MALE, address, List.of("Computer science"), BigDecimal.TEN, LocalDateTime.now());

        studentRepository.findStudentByEmail(email).ifPresentOrElse(s -> {
            log.info(s + " already exists");
        }, () -> {
            log.info("Inserting student " + student);
            studentRepository.insert(student);
        });
    }

    private void studentTestUsingMongoTemplateAndQuery() {
        String email = "ali.alavi@gmail.com";
        Address address = new Address("Iran", "Tehran", "+98");
        Student student = new Student("Ali", "Alavi", email, Gender.MALE, address, List.of("Computer science"), BigDecimal.TEN, LocalDateTime.now());

        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(student.getEmail()));
        List<Student> students = mongoTemplate.find(query, Student.class);

        if (students.size() > 1) {
            throw new IllegalStateException("found many students with email " + student.getEmail());
        }

        if (students.isEmpty()) {
            log.info("Inserting student " + student);
            mongoTemplate.insert(student);
        }
    }

    private void productTest() {
        productService.deleteAll();

        Map<String, Object> additionalProps1 = new HashMap<>();
        additionalProps1.put("manufacturer", "Apple");
        additionalProps1.put("warranty", "1 year");

        Map<String, Object> additionalProps2 = new HashMap<>();
        additionalProps2.put("weight", "1.5kg");
        additionalProps2.put("color", "black");

        Product product1 = Product.builder().name("iPhone 12").price(999.99).additionalProperties(additionalProps1).build();
        Product product2 = Product.builder().name("Laptop").price(1200.00).additionalProperties(additionalProps2).build();

        productService.save(product1);
        productService.save(product2);
        log.info("Products with unstructured data inserted.");

        List<Product> appleProducts = productService.findProductsByManufacturer("Apple");
        log.info("Fetching products from Apple:");
        appleProducts.forEach(product ->
                log.info(product.getName() + ": " + product.getPrice() + " USD")
        );
    }
}