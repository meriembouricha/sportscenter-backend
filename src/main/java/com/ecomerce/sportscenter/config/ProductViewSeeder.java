package com.ecomerce.sportscenter.config;

import com.ecomerce.sportscenter.entity.AppUser;
import com.ecomerce.sportscenter.entity.Product;
import com.ecomerce.sportscenter.entity.ProductView;
import com.ecomerce.sportscenter.repository.ProductRepository;
import com.ecomerce.sportscenter.repository.ProductViewRepository;
import com.ecomerce.sportscenter.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component  // uncomment to enable auto-run at startup
@RequiredArgsConstructor
public class ProductViewSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductViewRepository productViewRepository;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        // Fetch 40 users
        List<AppUser> users = userRepository.findAll();
        if (users.isEmpty()) {
            System.out.println("⚠️ No users found! Skipping ProductView seeding.");
            return;
        }

        // Fetch products within range [15775, 17521]
        List<Product> products = productRepository.findAllByIdBetween(15775, 17521);
        if (products.isEmpty()) {
            System.out.println("⚠️ No products found in the given range!");
            return;
        }

        System.out.println("Seeding ProductViews...");

        for (AppUser user : users) {
            int viewCount = 200 + random.nextInt(300); // 200–500 views per user

            for (int i = 0; i < viewCount; i++) {
                Product product = products.get(random.nextInt(products.size()));

                // Random view time in last 60 days
                LocalDateTime viewedAt = LocalDateTime.now()
                        .minusDays(random.nextInt(60))
                        .withHour(random.nextInt(24))
                        .withMinute(random.nextInt(60));

                productViewRepository.save(ProductView.builder()
                        .user(user)
                        .product(product)
                        .viewedAt(viewedAt)
                        .build());
            }
        }

        System.out.println("✅ Finished seeding ProductViews!");
    }
}
