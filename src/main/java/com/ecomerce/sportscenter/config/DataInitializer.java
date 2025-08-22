package com.ecomerce.sportscenter.config;
import com.ecomerce.sportscenter.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.ecomerce.sportscenter.entity.*;

import java.time.LocalDateTime;
import java.util.*;

// @Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final TypeRepository typeRepository;
    private final FeedbackRepository feedbackRepository;
    private final ProductViewRepository productViewRepository;
    private final PasswordEncoder passwordEncoder;

    private final Random random = new Random();

    private final String[] types = {
            "Running", "Trail Running", "Walking", "Nordic Walking", "Fitness", "Bodybuilding", "Yoga", "Pilates", "Boxing",
            "Karate", "Judo", "Taekwondo", "Cross Training", "Gymnastics", "Dance", "Football", "Basketball", "Volleyball",
            "Handball", "Rugby", "Hockey", "Ice Hockey", "Baseball", "Softball", "American Football", "Tennis",
            "Table Tennis", "Badminton", "Squash", "Padel", "Hiking", "Trekking", "Climbing", "Bouldering", "Camping",
            "Orienteering", "Hunting", "Archery", "Horse Riding", "Swimming", "Surfing", "Kayaking", "Canoeing", "Diving",
            "Snorkeling", "Sailing", "Paddleboarding", "Water Polo", "Fishing", "Skiing", "Snowboarding", "Ice Skating",
            "Sledding", "Road Cycling", "Mountain Biking", "BMX", "Urban Biking", "Indoor Cycling", "Golf", "Darts",
            "Billiards", "Petanque", "Boules", "Skateboarding", "Rollerblading", "Inline Skating", "Scootering", "Parkour",
            "Freerun", "Trampoline"
    };

    private final String[] brands = {
            "Nike", "Adidas", "Puma", "Under Armour", "Reebok", "Asics", "New Balance", "Mizuno", "Salomon", "Columbia"
    };

    private final String[] productNames = {
            "Shoes", "Jersey", "Shorts", "Backpack", "Gloves", "Ball", "Mat", "Helmet", "Goggles", "Board"
    };

    private final String[] feedbackComments = {
            "Excellent product!", "Very good quality.", "Not bad.", "Could be better.", "Did not like it.", "Will buy again!"
    };

    @Override
    public void run(String... args) throws Exception {

        if (brandRepository.count() > 0) {
            System.out.println("Database already seeded! Skipping DataInitializer...");
            return;
        }

        // Insert Types
        List<Type> savedTypes = Arrays.stream(types)
                .map(name -> Type.builder().name(name).build())
                .map(typeRepository::save)
                .toList();

        // Insert Brands
        List<Brand> savedBrands = Arrays.stream(brands)
                .map(name -> Brand.builder().name(name).build())
                .map(brandRepository::save)
                .toList();

        // Generate Products (10 per Type+Brand)
        List<Product> allProducts = new ArrayList<>();
        for (Type type : savedTypes) {
            for (Brand brand : savedBrands) {
                for (int i = 0; i < 10; i++) {
                    String pname = brand.getName() + " " + productNames[random.nextInt(productNames.length)] + " for " + type.getName();
                    Product product = Product.builder()
                            .name(pname)
                            .description("Best " + type.getName() + " gear by " + brand.getName())
                            .price(50L + random.nextInt(200))
                            .qunatity(100)
                            .pictureUrl("https://dummyimage.com/200x200/000/fff&text=" + pname.replace(" ", "+"))
                            .brand(brand)
                            .type(type)
                            .build();
                    allProducts.add(productRepository.save(product));
                }
            }
        }

        // Generate Users
        List<AppUser> users = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {
            AppUser user = AppUser.builder()
                    .username("user" + i)
                    .email("user" + i + "@test.com")
                    .password(passwordEncoder.encode("pass123456789"))
                    .enabled(true)
                    .build();
            users.add(userRepository.save(user));
        }

        // Generate Product Views and Feedback
        // Generate Product Views and Feedback (more views per user)
        for (AppUser user : users) {
            Set<String> viewedKeys = new HashSet<>(); // For tracking unique user-product-date keys

            int viewCount = 100 + random.nextInt(100); // 100–200 views per user

            for (int i = 0; i < viewCount; i++) {
                Product product = allProducts.get(random.nextInt(allProducts.size()));

                // Simulate a view at a random time in the past 30 days
                LocalDateTime viewedAt = LocalDateTime.now().minusDays(random.nextInt(30)).withHour(random.nextInt(24)).withMinute(random.nextInt(60));

                String viewKey = user.getId() + "-" + product.getId() + "-" + viewedAt.toLocalDate();

                // Optionally skip if we've already created a view on the same day for same user-product
                if (viewedKeys.contains(viewKey)) continue;
                viewedKeys.add(viewKey);

                productViewRepository.save(ProductView.builder()
                        .user(user)
                        .product(product)
                        .viewedAt(viewedAt)
                        .build());

                // Optional: Leave feedback randomly
                if (random.nextInt(100) < 20) { // 20% chance
                    feedbackRepository.save(Feedback.builder()
                            .user(user)
                            .product(product)
                            .rate(1 + random.nextInt(5))
                            .descFeedback(feedbackComments[random.nextInt(feedbackComments.length)])
                            .build());
                }
            }
        }

        System.out.println("✅ Seeded database with types, brands, products, users, views and feedback!");
    }
}
