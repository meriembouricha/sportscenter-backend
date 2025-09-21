package com.ecomerce.sportscenter.config;

import com.ecomerce.sportscenter.entity.AppUser;
import com.ecomerce.sportscenter.entity.Feedback;
import com.ecomerce.sportscenter.entity.Product;
import com.ecomerce.sportscenter.repository.FeedbackRepository;
import com.ecomerce.sportscenter.repository.ProductRepository;
import com.ecomerce.sportscenter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Log4j2
public class FeedbackDataInitializerForce implements CommandLineRunner {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final Random random = new Random();

    // Sample feedback descriptions
    private final String[] feedbackDescriptions = {
        "Excellent product! Highly recommend it.",
        "Very good quality and fast delivery.",
        "Good product, meets expectations.",
        "Not bad, could be better though.",
        "Average product, nothing special.",
        "Disappointed with the quality.",
        "Great value for money!",
        "Perfect fit and comfortable.",
        "Good customer service and product.",
        "Will definitely buy again!",
        "Amazing quality, exceeded expectations.",
        "Good product but shipping was slow.",
        "Love it! Exactly what I needed.",
        "Decent product for the price.",
        "Could be improved but overall okay.",
        "Fantastic! Better than expected.",
        "Good product, would recommend.",
        "Not the best but not the worst.",
        "Excellent customer service!",
        "Great product, very satisfied."
    };

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting FORCED Feedback Data Initialization...");
        
        // Log current feedback count
        long currentFeedbackCount = feedbackRepository.count();
        log.info("Current feedback count in database: {}", currentFeedbackCount);

        // Clear existing feedbacks if any
        if (currentFeedbackCount > 0) {
            log.info("Clearing existing feedbacks...");
            feedbackRepository.deleteAll();
            log.info("Cleared {} existing feedbacks", currentFeedbackCount);
        }

        // Get all users (IDs 2-43)
        List<AppUser> allUsers = userRepository.findAll();
        log.info("Total users in database: {}", allUsers.size());
        
        List<AppUser> users = allUsers.stream()
                .filter(user -> user.getId() >= 2 && user.getId() <= 43)
                .toList();

        log.info("Users in range 2-43: {}", users.size());
        if (users.isEmpty()) {
            log.warn("No users found in the specified range (2-43). Please ensure users exist first.");
            // Log some user IDs for debugging
            allUsers.stream().limit(10).forEach(user -> log.info("User ID: {}", user.getId()));
            return;
        }

        // Get all products (IDs 15775-17521)
        List<Product> allProducts = productRepository.findAll();
        log.info("Total products in database: {}", allProducts.size());
        
        List<Product> products = allProducts.stream()
                .filter(product -> product.getId() >= 15775 && product.getId() <= 17521)
                .toList();

        log.info("Products in range 15775-17521: {}", products.size());
        if (products.isEmpty()) {
            log.warn("No products found in the specified range (15775-17521). Please ensure products exist first.");
            // Log some product IDs for debugging
            allProducts.stream().limit(10).forEach(product -> log.info("Product ID: {}", product.getId()));
            return;
        }

        log.info("Found {} users and {} products for feedback generation", users.size(), products.size());

        List<Feedback> feedbacksToCreate = new ArrayList<>();
        int totalFeedbacks = 0;

        // Generate 10 feedbacks per user
        for (AppUser user : users) {
            for (int i = 0; i < 10; i++) {
                // Select a random product
                Product randomProduct = products.get(random.nextInt(products.size()));
                
                // Generate random rating (1-5)
                int rating = 1 + random.nextInt(5);
                
                // Select random feedback description
                String description = feedbackDescriptions[random.nextInt(feedbackDescriptions.length)];
                
                // Create feedback
                Feedback feedback = Feedback.builder()
                        .user(user)
                        .product(randomProduct)
                        .rate(rating)
                        .descFeedback(description)
                        .build();
                
                feedbacksToCreate.add(feedback);
                totalFeedbacks++;
            }
        }

        // Save all feedbacks in batches for better performance
        int batchSize = 100;
        for (int i = 0; i < feedbacksToCreate.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, feedbacksToCreate.size());
            List<Feedback> batch = feedbacksToCreate.subList(i, endIndex);
            feedbackRepository.saveAll(batch);
            log.info("Saved batch {}/{} feedbacks", endIndex, totalFeedbacks);
        }

        log.info("✅ Successfully created {} feedbacks for {} users", totalFeedbacks, users.size());
        
        // Log some statistics
        long totalFeedbacksInDb = feedbackRepository.count();
        log.info("Total feedbacks in database: {}", totalFeedbacksInDb);
    }
}

