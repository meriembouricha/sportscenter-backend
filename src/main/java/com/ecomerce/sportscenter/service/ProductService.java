package com.ecomerce.sportscenter.service;

import com.ecomerce.sportscenter.entity.Product;
import com.ecomerce.sportscenter.model.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponse getProductById(Integer productId);
    Page<ProductResponse> getProducts(Pageable pageable);
    List<ProductResponse> searchProductsByName(String keyword);
    List<ProductResponse> searchProductsByBrand(Integer brandId);
    List<ProductResponse> searchProductsByType(Integer typeId);
    List<ProductResponse> searchProductsByBrandAndType(Integer brandId, Integer typeId);
    List<ProductResponse> searchProductsByBrandTypeAndName(Integer brandId, Integer typeId, String keyword);
    ProductResponse createProduct(Product product);
    ProductResponse updateProduct(Integer productId, Product product);
    void deleteProduct(Integer productId);
    void incrementQuantity(Integer productId, Integer amount);
    void decrementQuantity(Integer productId, Integer amount);



}
