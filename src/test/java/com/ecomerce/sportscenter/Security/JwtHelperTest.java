package com.ecomerce.sportscenter.Security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtHelperTest {

    private JwtHelper jwtHelper;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtHelper = new JwtHelper();
        userDetails = User.builder()
                .username("testuser")
                .password("password")
                .authorities(new ArrayList<>())
                .build();
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        // When
        String token = jwtHelper.generateToken(userDetails, 1L);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void getUserNameFromToken_ShouldReturnCorrectUsername() {
        // Given
        String token = jwtHelper.generateToken(userDetails, 1L);

        // When
        String username = jwtHelper.getUserNameFromToken(token);

        // Then
        assertEquals("testuser", username);
    }

    @Test
    void getUserIdFromToken_ShouldReturnCorrectUserId() {
        // Given
        String token = jwtHelper.generateToken(userDetails, 1L);

        // When
        Long userId = jwtHelper.getUserIdFromToken(token);

        // Then
        assertEquals(1L, userId);
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        // Given
        String token = jwtHelper.generateToken(userDetails, 1L);

        // When
        boolean isValid = jwtHelper.validateToken(token, userDetails);

        // Then
        assertTrue(isValid);
    }

    @Test
    void validateToken_WithInvalidUsername_ShouldReturnFalse() {
        // Given
        String token = jwtHelper.generateToken(userDetails, 1L);
        UserDetails differentUser = User.builder()
                .username("differentuser")
                .password("password")
                .authorities(new ArrayList<>())
                .build();

        // When
        boolean isValid = jwtHelper.validateToken(token, differentUser);

        // Then
        assertFalse(isValid);
    }

    @Test
    void getExpirationDateFromToken_ShouldReturnFutureDate() {
        // Given
        String token = jwtHelper.generateToken(userDetails, 1L);

        // When
        Date expirationDate = jwtHelper.getExpirationDateFromToken(token);

        // Then
        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }

    @Test
    void extractTokenFromHeader_WithBearerToken_ShouldReturnToken() {
        // Given
        String tokenHeader = "Bearer eyJhbGciOiJIUzUxMiJ9...";

        // When
        String token = jwtHelper.extractTokenFromHeader(tokenHeader);

        // Then
        assertEquals("eyJhbGciOiJIUzUxMiJ9...", token);
    }

    @Test
    void extractTokenFromHeader_WithoutBearer_ShouldReturnNull() {
        // Given
        String tokenHeader = "eyJhbGciOiJIUzUxMiJ9...";

        // When
        String token = jwtHelper.extractTokenFromHeader(tokenHeader);

        // Then
        assertNull(token);
    }

    @Test
    void extractTokenFromHeader_WithNull_ShouldReturnNull() {
        // When
        String token = jwtHelper.extractTokenFromHeader(null);

        // Then
        assertNull(token);
    }

    @Test
    void getUserIdFromToken_WithIntegerId_ShouldReturnLong() {
        // Given
        String token = jwtHelper.generateToken(userDetails, 1L);

        // When
        Long userId = jwtHelper.getUserIdFromToken(token);

        // Then
        assertEquals(1L, userId);
    }
}
