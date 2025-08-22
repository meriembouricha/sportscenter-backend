package com.ecomerce.sportscenter.controller;

import com.ecomerce.sportscenter.Security.JwtHelper;
import com.ecomerce.sportscenter.config.AuthService;
import com.ecomerce.sportscenter.entity.AppUser;
import com.ecomerce.sportscenter.entity.Roles;
import com.ecomerce.sportscenter.entity.SignupRequest;
import com.ecomerce.sportscenter.model.JwtRequest;
import com.ecomerce.sportscenter.model.JwtResponse;
import com.ecomerce.sportscenter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager manager;
    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;

    @Autowired
    public AuthController(AuthService authService, AuthenticationManager manager, UserRepository userRepository, JwtHelper jwtHelper) {
        this.authService = authService;
        this.manager = manager;
        this.userRepository = userRepository;
        this.jwtHelper = jwtHelper;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody SignupRequest request) {
        try {
            String response = authService.registerUser(request.getUsername(), request.getEmail(), request.getPassword(), Roles.USER);
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", response);
            return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        // Vérification si l'utilisateur existe
        AppUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        // Authentification
        this.authenticate(request.getUsername(), request.getPassword());

        // Génération du token avec l'ID
        UserDetails userDetails = authService.loadUserByUsername(request.getUsername());
        String token = this.jwtHelper.generateToken(userDetails, user.getId());

        // Récupérer le rôle de l'utilisateur
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");

        // Création du JwtResponse
        JwtResponse response = new JwtResponse(userDetails.getUsername(), token, role);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<UserDetails> getUserDetails(@RequestHeader("Authorization") String tokenHeader) {
        String token = extractTokenFromHeader(tokenHeader);
        if (token != null) {
            String username = jwtHelper.getUserNameFromToken(token);
            UserDetails userDetails = authService.loadUserByUsername(username);
            return new ResponseEntity<>(userDetails, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private String extractTokenFromHeader(String tokenHeader) {
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            return tokenHeader.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }

    private void authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            manager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Nom d'utilisateur ou mot de passe incorrect");
        }
    }
}
