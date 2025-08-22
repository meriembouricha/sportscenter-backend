package com.ecomerce.sportscenter.config;

import com.ecomerce.sportscenter.entity.AppUser;
import com.ecomerce.sportscenter.entity.Roles;
import com.ecomerce.sportscenter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Vérifie si un administrateur existe déjà dans la base de données
        if (!userRepository.existsByUsername("admin")) {
            // Crée un administrateur principal si aucun administrateur n'existe
            AppUser admin = AppUser.builder()
                    .username("admin")
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("adminpassword")) // Mot de passe sécurisé
                    .roles(Set.of(Roles.ADMIN)) // Attribution du rôle ADMIN
                    .build();

            userRepository.save(admin);
            System.out.println("Admin principal créé avec succès");
        }
    }
}
