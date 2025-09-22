package com.ecomerce.sportscenter.controller;

import com.ecomerce.sportscenter.entity.AppUser;
import com.ecomerce.sportscenter.entity.Roles;
import com.ecomerce.sportscenter.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private AppUser testUser;

    @BeforeEach
    void setUp() {
        testUser = AppUser.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("hashedpassword")
                .roles(Set.of(Roles.USER))
                .enabled(true)
                .build();
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() throws Exception {
        // Given
        List<AppUser> users = Arrays.asList(testUser);
        when(userService.getAllUsers()).thenReturn(users);

        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].enabled").value(true));

        verify(userService).getAllUsers();
    }

    @Test
    void getUserByUsername_WhenUserExists_ShouldReturnUser() throws Exception {
        // Given
        when(userService.getUserByUsername("testuser")).thenReturn(testUser);

        // When & Then
        mockMvc.perform(get("/api/users/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService).getUserByUsername("testuser");
    }

    @Test
    void userExists_WhenUserExists_ShouldReturnTrue() throws Exception {
        // Given
        when(userService.userExistsByUsername("testuser")).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/users/exists/testuser"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(userService).userExistsByUsername("testuser");
    }

    @Test
    void userExists_WhenUserNotExists_ShouldReturnFalse() throws Exception {
        // Given
        when(userService.userExistsByUsername("nonexistent")).thenReturn(false);

        // When & Then
        mockMvc.perform(get("/api/users/exists/nonexistent"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(userService).userExistsByUsername("nonexistent");
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        // Given
        when(userService.saveUser(any(AppUser.class))).thenReturn(testUser);

        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService).saveUser(any(AppUser.class));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        // Given
        when(userService.saveUser(any(AppUser.class))).thenReturn(testUser);

        // When & Then
        mockMvc.perform(put("/api/users/testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(userService).saveUser(any(AppUser.class));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(userService).deleteUser(1L);

        // When & Then
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    void deleteUser_WhenUserNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        doThrow(new RuntimeException("Utilisateur non trouvé")).when(userService).deleteUser(999L);

        // When & Then
        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(999L);
    }
}
