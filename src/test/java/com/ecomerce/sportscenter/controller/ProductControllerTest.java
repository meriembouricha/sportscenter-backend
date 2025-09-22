package com.ecomerce.sportscenter.controller;

import com.ecomerce.sportscenter.entity.Brand;
import com.ecomerce.sportscenter.entity.Product;
import com.ecomerce.sportscenter.entity.Type;
import com.ecomerce.sportscenter.model.ProductResponse;
import com.ecomerce.sportscenter.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductResponse testProductResponse;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProductResponse = ProductResponse.builder()
                .id(1)
                .name("Test Product")
                .description("Test Description")
                .price(10000L)
                .pictureUrl("https://example.com/test.jpg")
                .productQuantity(10)
                .productBrand("Nike")
                .productType("Shoes")
                .build();

        testProduct = Product.builder()
                .id(1)
                .name("Test Product")
                .description("Test Description")
                .price(10000L)
                .qunatity(10)
                .build();
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() throws Exception {
        // Given
        when(productService.getProductById(1)).thenReturn(testProductResponse);

        // When & Then
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.price").value(10000));

        verify(productService).getProductById(1);
    }

    @Test
    void getProducts_ShouldReturnPagedProducts() throws Exception {
        // Given
        Page<ProductResponse> productPage = new PageImpl<>(Arrays.asList(testProductResponse));
        when(productService.getProducts(any())).thenReturn(productPage);

        // When & Then
        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Test Product"));

        verify(productService).getProducts(any());
    }

    @Test
    void searchProductsByName_ShouldReturnMatchingProducts() throws Exception {
        // Given
        List<ProductResponse> products = Arrays.asList(testProductResponse);
        when(productService.searchProductsByName("Test")).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products/search")
                        .param("name", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test Product"));

        verify(productService).searchProductsByName("Test");
    }

    @Test
    void searchProductsByBrand_ShouldReturnProductsByBrand() throws Exception {
        // Given
        List<ProductResponse> products = Arrays.asList(testProductResponse);
        when(productService.searchProductsByBrand(1)).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products/search")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test Product"));

        verify(productService).searchProductsByBrand(1);
    }

    @Test
    void searchProductsByType_ShouldReturnProductsByType() throws Exception {
        // Given
        List<ProductResponse> products = Arrays.asList(testProductResponse);
        when(productService.searchProductsByType(1)).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products/search")
                        .param("typeId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test Product"));

        verify(productService).searchProductsByType(1);
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        // Given
        when(productService.createProduct(any(Product.class))).thenReturn(testProductResponse);

        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productService).createProduct(any(Product.class));
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct() throws Exception {
        // Given
        when(productService.updateProduct(anyInt(), any(Product.class))).thenReturn(testProductResponse);

        // When & Then
        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productService).updateProduct(1, testProduct);
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(productService).deleteProduct(1);

        // When & Then
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(productService).deleteProduct(1);
    }

    @Test
    void incrementQuantity_ShouldReturnOk() throws Exception {
        // Given
        doNothing().when(productService).incrementQuantity(1, 5);

        // When & Then
        mockMvc.perform(put("/api/products/1/increment")
                        .param("amount", "5"))
                .andExpect(status().isOk());

        verify(productService).incrementQuantity(1, 5);
    }

    @Test
    void decrementQuantity_ShouldReturnOk() throws Exception {
        // Given
        doNothing().when(productService).decrementQuantity(1, 3);

        // When & Then
        mockMvc.perform(put("/api/products/1/decrement")
                        .param("amount", "3"))
                .andExpect(status().isOk());

        verify(productService).decrementQuantity(1, 3);
    }
}
