package com.ecomerce.sportscenter.service;

import com.ecomerce.sportscenter.entity.AppUser;
import com.ecomerce.sportscenter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<AppUser> getAllUsers() {
        return userRepository.findAll(); // Récupère tous les utilisateurs
    }

    public AppUser getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null); // Retourne l'utilisateur par son nom d'utilisateur
    }

    public AppUser getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null); // Retourne l'utilisateur par son email
    }

    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username); // Vérifie si l'utilisateur existe déjà par son nom
    }

    public AppUser saveUser(AppUser user) {
        return userRepository.save(user); // Sauvegarde un utilisateur
    }

    // UserService.java
    public void deleteUser(Long id) {
        // Vérifie si l'utilisateur existe
        AppUser user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        // Supprime l'utilisateur
        userRepository.delete(user);
    }

    public AppUser deactivateUser(Long id) {
        AppUser user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(false);  // Désactive l'utilisateur
        return userRepository.save(user);  // Sauvegarde l'utilisateur
    }



}
