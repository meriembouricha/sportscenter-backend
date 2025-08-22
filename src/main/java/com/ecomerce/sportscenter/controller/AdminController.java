package com.ecomerce.sportscenter.controller;

import com.ecomerce.sportscenter.config.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AuthService authService;

    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    @PutMapping("/assignRole")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> assignRole(@RequestParam String username, @RequestParam String role) {
        try {
            authService.assignRole(username, role);
            return ResponseEntity.ok("Role assigned successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error assigning role: " + e.getMessage());
        }
    }
}
