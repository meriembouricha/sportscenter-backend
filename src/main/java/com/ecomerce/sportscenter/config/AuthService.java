package com.ecomerce.sportscenter.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.ecomerce.sportscenter.Security.JwtTokenProvider;
import com.ecomerce.sportscenter.entity.Roles;
import com.ecomerce.sportscenter.entity.AppUser;  // Utilisation de AppUser
import com.ecomerce.sportscenter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public String registerUser(String username, String email, String password, Roles role) {
        if (userRepository.findByUsername(username).isPresent() || userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Nom d'utilisateur ou email déjà utilisé");
        }

        // Vérification du rôle, par défaut USER
        Roles userRole = (role != null) ? role : Roles.USER;

        AppUser newUser = AppUser.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password)) // Hash du mot de passe
                .roles(Set.of(userRole))
                .build();

        userRepository.save(newUser);
        return "Utilisateur créé avec succès";
    }


    public String authenticateUser(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        // Générer le token
        String token = jwtTokenProvider.generateToken(user);

        // Récupérer l'URL de redirection selon le rôle
        String redirectUrl = user.getRoles().contains(Roles.ADMIN) ? "/admin" : "/store";

        // Optionnel : retourner le rôle et la redirection dans la réponse
        return "{\"username\": \"" + user.getUsername() + "\", \"token\": \"" + token + "\", \"role\": \"" + user.getRoles().iterator().next().name() + "\", \"redirectUrl\": \"" + redirectUrl + "\"}";
    }





    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));

        return new org.springframework.security.core.userdetails.User(
                appUser.getUsername(),
                appUser.getPassword(),
                mapRolesToAuthorities(appUser.getRoles()) // Assure-toi de mapper les rôles
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Roles> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }

    public void assignRole(String username, String role) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        Roles newRole = Roles.valueOf(role.toUpperCase());
        user.getRoles().add(newRole); // Ajoute le nouveau rôle
        userRepository.save(user);
    }

}
