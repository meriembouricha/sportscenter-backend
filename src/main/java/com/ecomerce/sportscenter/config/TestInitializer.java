package com.ecomerce.sportscenter.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1) // Run first
public class TestInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🔥 TEST INITIALIZER IS RUNNING! 🔥");
        System.out.println("This proves CommandLineRunner is working!");
    }
}
