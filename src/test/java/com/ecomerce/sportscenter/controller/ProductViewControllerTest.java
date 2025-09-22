package com.ecomerce.sportscenter.controller;

import com.ecomerce.sportscenter.entity.AppUser;
import com.ecomerce.sportscenter.entity.Product;
import com.ecomerce.sportscenter.entity.ProductView;
import com.ecomerce.sportscenter.entity.Roles;
import com.ecomerce.sportscenter.model.ProductViewRequest;
import com.ecomerce.sportscenter.service.ProductViewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductViewController.class)
@ActiveProfiles("test")
class ProductViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductViewService productViewService;

    @Autowired
    private ObjectMapper objectMapper;

    private AppUser testUser;
    private Product testProduct;
    private ProductView testProductView;
    private ProductViewRequest testRequest;

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

        testProduct = Product.builder()
                .id(1)
                .name("Test Product")
                .description("Test Description")
                .price(10000L)
                .qunatity(5)
                .build();

        testProductView = ProductView.builder()
                .id(1L)
                .user(testUser)
                .product(testProduct)
                .viewedAt(LocalDateTime.now())
                .build();

        testRequest = new ProductViewRequest();
        testRequest.setUser(testUser);
        testRequest.setProduct(testProduct);
    }

    @Test
    void createProductView_ShouldReturnCreatedProductView() throws Exception {
        // Given
        when(productViewService.saveProductView(any(AppUser.class), any(Product.class)))
                .thenReturn(testProductView);

        // When & Then
        mockMvc.perform(post("/api/product-views")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.user.username").value("testuser"))
                .andExpect(jsonPath("$.product.name").value("Test Product"));

        verify(productViewService).saveProductView(any(AppUser.class), any(Product.class));
    }

    @Test
    void getAllViews_ShouldReturnAllProductViews() throws Exception {
        // Given
        List<ProductView> productViews = Arrays.asList(testProductView);
        when(productViewService.getAllViews()).thenReturn(productViews);

        // When & Then
        mockMvc.perform(get("/api/product-views"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].user.username").value("testuser"))
                .andExpect(jsonPath("$[0].product.name").value("Test Product"));

        verify(productViewService).getAllViews();
    }

    @Test
    void deleteView_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(productViewService).deleteView(1L);

        // When & Then
        mockMvc.perform(delete("/api/product-views/1"))
                .andExpect(status().isNoContent());

        verify(productViewService).deleteView(1L);
    }

    @Test
    void deleteView_WithInvalidId_ShouldStillReturnNoContent() throws Exception {
        // Given
        doNothing().when(productViewService).deleteView(999L);

        // When & Then
        mockMvc.perform(delete("/api/product-views/999"))
                .andExpect(status().isNoContent());

        verify(productViewService).deleteView(999L);
    }
}
