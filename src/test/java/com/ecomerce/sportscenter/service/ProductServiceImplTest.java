package com.ecomerce.sportscenter.service;

import com.ecomerce.sportscenter.entity.Brand;
import com.ecomerce.sportscenter.entity.Product;
import com.ecomerce.sportscenter.entity.Type;
import com.ecomerce.sportscenter.exceptions.ProductNotFoundException;
import com.ecomerce.sportscenter.model.ProductResponse;
import com.ecomerce.sportscenter.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;
    private Brand testBrand;
    private Type testType;

    @BeforeEach
    void setUp() {
        testBrand = Brand.builder()
                .id(1)
                .name("Nike")
                .build();

        testType = Type.builder()
                .id(1)
                .name("Shoes")
                .build();

        testProduct = Product.builder()
                .id(1)
                .name("Air Max")
                .description("Comfortable running shoes")
                .price(15000L)
                .pictureUrl("https://example.com/airmax.jpg")
                .qunatity(10)
                .brand(testBrand)
                .type(testType)
                .build();
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProductResponse() {
        // Given
        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));

        // When
        ProductResponse result = productService.getProductById(1);

        // Then
        assertNotNull(result);
        assertEquals("Air Max", result.getName());
        assertEquals("Comfortable running shoes", result.getDescription());
        assertEquals(15000L, result.getPrice());
        verify(productRepository).findById(1);
    }

    @Test
    void getProductById_WhenProductNotExists_ShouldThrowException() {
        // Given
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductById(999);
        });
        assertEquals("Product with given id doesn't exist", exception.getMessage());
        verify(productRepository).findById(999);
    }

    @Test
    void getProducts_ShouldReturnPagedProductResponses() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Arrays.asList(testProduct));
        when(productRepository.findAll(pageable)).thenReturn(productPage);

        // When
        Page<ProductResponse> result = productService.getProducts(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Air Max", result.getContent().get(0).getName());
        verify(productRepository).findAll(pageable);
    }

    @Test
    void searchProductsByName_ShouldReturnMatchingProducts() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.searchByName("Air")).thenReturn(products);

        // When
        List<ProductResponse> result = productService.searchProductsByName("Air");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Air Max", result.get(0).getName());
        verify(productRepository).searchByName("Air");
    }

    @Test
    void searchProductsByBrand_ShouldReturnProductsByBrand() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.searchByBrand(1)).thenReturn(products);

        // When
        List<ProductResponse> result = productService.searchProductsByBrand(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Air Max", result.get(0).getName());
        verify(productRepository).searchByBrand(1);
    }

    @Test
    void searchProductsByType_ShouldReturnProductsByType() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.searchByType(1)).thenReturn(products);

        // When
        List<ProductResponse> result = productService.searchProductsByType(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Air Max", result.get(0).getName());
        verify(productRepository).searchByType(1);
    }

    @Test
    void searchProductsByBrandAndType_ShouldReturnMatchingProducts() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.searchByBrandAndType(1, 1)).thenReturn(products);

        // When
        List<ProductResponse> result = productService.searchProductsByBrandAndType(1, 1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Air Max", result.get(0).getName());
        verify(productRepository).searchByBrandAndType(1, 1);
    }

    @Test
    void createProduct_ShouldReturnCreatedProductResponse() {
        // Given
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        ProductResponse result = productService.createProduct(testProduct);

        // Then
        assertNotNull(result);
        assertEquals("Air Max", result.getName());
        verify(productRepository).save(testProduct);
    }

    @Test
    void updateProduct_WhenProductExists_ShouldReturnUpdatedProductResponse() {
        // Given
        Product updatedProduct = Product.builder()
                .id(1)
                .name("Air Max Updated")
                .description("Updated description")
                .price(16000L)
                .qunatity(15)
                .build();

        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // When
        ProductResponse result = productService.updateProduct(1, updatedProduct);

        // Then
        assertNotNull(result);
        assertEquals("Air Max Updated", result.getName());
        verify(productRepository).findById(1);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_WhenProductNotExists_ShouldThrowException() {
        // Given
        Product updatedProduct = Product.builder()
                .id(999)
                .name("Updated Product")
                .build();

        when(productRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(999, updatedProduct);
        });
        assertEquals("Product not found", exception.getMessage());
        verify(productRepository).findById(999);
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProduct_WhenProductExists_ShouldDeleteProduct() {
        // Given
        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));
        doNothing().when(productRepository).delete(testProduct);

        // When
        productService.deleteProduct(1);

        // Then
        verify(productRepository).findById(1);
        verify(productRepository).delete(testProduct);
    }

    @Test
    void deleteProduct_WhenProductNotExists_ShouldThrowException() {
        // Given
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.deleteProduct(999);
        });
        assertEquals("Product not found", exception.getMessage());
        verify(productRepository).findById(999);
        verify(productRepository, never()).delete(any());
    }

    @Test
    void incrementQuantity_WhenProductExists_ShouldIncrementQuantity() {
        // Given
        when(productRepository.existsById(1)).thenReturn(true);

        // When
        productService.incrementQuantity(1, 5);

        // Then
        verify(productRepository).existsById(1);
        verify(productRepository).incrementProductQuantity(1, 5);
    }

    @Test
    void decrementQuantity_WhenProductExists_ShouldDecrementQuantity() {
        // Given
        Product productToDecrement = Product.builder()
                .id(1)
                .name("Air Max")
                .description("Comfortable running shoes")
                .price(15000L)
                .pictureUrl("https://example.com/airmax.jpg")
                .qunatity(10)
                .brand(testBrand)
                .type(testType)
                .build();
        
        when(productRepository.findById(1)).thenReturn(Optional.of(productToDecrement));

        // When
        productService.decrementQuantity(1, 3);

        // Then
        verify(productRepository).findById(1);
        verify(productRepository).decrementProductQuantity(1, 3);
    }
}
