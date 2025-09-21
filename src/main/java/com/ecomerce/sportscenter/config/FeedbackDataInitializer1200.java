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
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Order(200) // Run after other initializers
@RequiredArgsConstructor
@Log4j2
public class FeedbackDataInitializer1200 implements CommandLineRunner {

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
        "Great product, very satisfied.",
        "Outstanding quality and service!",
        "Very happy with this purchase.",
        "Good product, fast shipping.",
        "Could be better but acceptable.",
        "Perfect for my needs!",
        "Great value, would buy again.",
        "Not what I expected but okay.",
        "Excellent customer support!",
        "Very satisfied with quality.",
        "Good product overall."
    };

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🔥🔥🔥 FEEDBACK INITIALIZER 1200 STARTED! 🔥🔥🔥");
        log.info("Starting FORCED Feedback Data Initialization for 1200 feedbacks...");
        
        // Log current feedback count
        long currentFeedbackCount = feedbackRepository.count();
        System.out.println("Current feedback count: " + currentFeedbackCount);
        log.info("Current feedback count in database: {}", currentFeedbackCount);

        // Clear existing feedbacks to ensure we create exactly 1200
        if (currentFeedbackCount > 0) {
            System.out.println("Clearing existing feedbacks to create fresh 1200...");
            log.info("Clearing {} existing feedbacks...", currentFeedbackCount);
            feedbackRepository.deleteAll();
            System.out.println("✅ Cleared all existing feedbacks!");
        }

        // Get all users (IDs 2-43)
        List<AppUser> allUsers = userRepository.findAll();
        System.out.println("Total users in database: " + allUsers.size());
        log.info("Total users in database: {}", allUsers.size());
        
        List<AppUser> users = allUsers.stream()
                .filter(user -> user.getId() >= 2 && user.getId() <= 43)
                .toList();

        System.out.println("Users in range 2-43: " + users.size());
        log.info("Users in range 2-43: {}", users.size());
        
        if (users.isEmpty()) {
            System.out.println("❌ No users found in range 2-43!");
            log.warn("No users found in the specified range (2-43). Please ensure users exist first.");
            // Log some user IDs for debugging
            allUsers.stream().limit(10).forEach(user -> {
                System.out.println("User ID: " + user.getId());
                log.info("User ID: {}", user.getId());
            });
            return;
        }

        // Get all products (IDs 15775-17521)
        List<Product> allProducts = productRepository.findAll();
        System.out.println("Total products in database: " + allProducts.size());
        log.info("Total products in database: {}", allProducts.size());
        
        List<Product> products = allProducts.stream()
                .filter(product -> product.getId() >= 15775 && product.getId() <= 17521)
                .toList();

        System.out.println("Products in range 15775-17521: " + products.size());
        log.info("Products in range 15775-17521: {}", products.size());
        
        if (products.isEmpty()) {
            System.out.println("❌ No products found in range 15775-17521!");
            log.warn("No products found in the specified range (15775-17521). Please ensure products exist first.");
            // Log some product IDs for debugging
            allProducts.stream().limit(10).forEach(product -> {
                System.out.println("Product ID: " + product.getId());
                log.info("Product ID: {}", product.getId());
            });
            return;
        }

        System.out.println("✅ Found " + users.size() + " users and " + products.size() + " products for feedback generation");
        log.info("Found {} users and {} products for feedback generation", users.size(), products.size());

        List<Feedback> feedbacksToCreate = new ArrayList<>();
        int targetFeedbacks = 1200; // Target number of feedbacks

        System.out.println("🚀 Starting to create " + targetFeedbacks + " feedbacks...");
        log.info("Starting to create {} feedbacks...", targetFeedbacks);

        // Generate exactly 1200 feedbacks
        for (int i = 0; i < targetFeedbacks; i++) {
            // Select a random user
            AppUser randomUser = users.get(random.nextInt(users.size()));
            
            // Select a random product
            Product randomProduct = products.get(random.nextInt(products.size()));
            
            // Generate random rating (1-5)
            int rating = 1 + random.nextInt(5);
            
            // Select random feedback description
            String description = feedbackDescriptions[random.nextInt(feedbackDescriptions.length)];
            
            // Create feedback
            Feedback feedback = Feedback.builder()
                    .user(randomUser)
                    .product(randomProduct)
                    .rate(rating)
                    .descFeedback(description)
                    .build();
            
            feedbacksToCreate.add(feedback);
            
            // Progress indicator every 100 feedbacks
            if ((i + 1) % 100 == 0) {
                System.out.println("Created " + (i + 1) + "/" + targetFeedbacks + " feedbacks...");
            }
        }

        System.out.println("💾 Starting to save " + feedbacksToCreate.size() + " feedbacks to database...");
        log.info("Starting to save {} feedbacks to database...", feedbacksToCreate.size());

        // Save all feedbacks in batches for better performance
        int batchSize = 100;
        int savedCount = 0;
        for (int i = 0; i < feedbacksToCreate.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, feedbacksToCreate.size());
            List<Feedback> batch = feedbacksToCreate.subList(i, endIndex);
            feedbackRepository.saveAll(batch);
            savedCount = endIndex;
            System.out.println("💾 Saved batch " + savedCount + "/" + targetFeedbacks + " feedbacks");
            log.info("Saved batch {}/{} feedbacks", savedCount, targetFeedbacks);
        }

        System.out.println("🎉 SUCCESS! Created " + savedCount + " feedbacks!");
        log.info("✅ Successfully created {} feedbacks (target was 1200)", savedCount);
        
        // Log final statistics
        long totalFeedbacksInDb = feedbackRepository.count();
        System.out.println("📊 Total feedbacks in database: " + totalFeedbacksInDb);
        log.info("Total feedbacks in database: {}", totalFeedbacksInDb);
        
        if (totalFeedbacksInDb == 1200) {
            System.out.println("🎯 PERFECT! Exactly 1200 feedbacks created!");
        } else {
            System.out.println("⚠️ WARNING: Expected 1200 feedbacks but found " + totalFeedbacksInDb);
        }
    }
}

