package com.ecomerce.sportscenter.service;


import com.ecomerce.sportscenter.entity.Product;
import com.ecomerce.sportscenter.exceptions.ProductNotFoundException;
import com.ecomerce.sportscenter.model.ProductResponse;
import com.ecomerce.sportscenter.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse getProductById(Integer productId) {
        log.info("Fetching Product by Id: {}", productId);
        Product product =productRepository.findById(productId)
                .orElseThrow(()->new ProductNotFoundException("Product with given id doesn't exist"));
        //now convert the product to product response
        ProductResponse productResponse = convertToProductResponse(product);
        log.info("Fetched Product by Id: {}", productId);
        return productResponse;
    }



    @Override
    public Page<ProductResponse> getProducts(Pageable pageable) {
        log.info("Fetching products");
        //Retrieve products from DB
        Page<Product> productPage = productRepository.findAll(pageable);
        //Map
        Page<ProductResponse> productResponses = productPage
                .map(this::convertToProductResponse);
        log.info("Fetched all products");
        return productResponses;
    }

    @Override
    public List<ProductResponse> searchProductsByName(String keyword) {
        log.info("Searching product(s) by name: {}", keyword);
        //Call the custom query Method
        List<Product> products = productRepository.searchByName(keyword);
        //Map
        List<ProductResponse> productResponses = products.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
        log.info("Fetched all products");
        return productResponses;
    }
    @Override
    public List<ProductResponse> searchProductsByBrand(Integer brandId) {
        log.info("Searching product(s) by brandId: {}", brandId);
        //Call the custom query Method
        List<Product> products = productRepository.searchByBrand(brandId);
        //Map
        List<ProductResponse> productResponses = products.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
        log.info("Fetched all products");
        return productResponses;
    }

    public List<ProductResponse> searchProductsByType(Integer typeId) {
        log.info("Searching product(s) by typeId: {}", typeId);
        //Call the custom query Method
        List<Product> products = productRepository.searchByType(typeId);
        //Map
        List<ProductResponse> productResponses = products.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
        log.info("Fetched all products");
        return productResponses;
    }



    @Override
    public List<ProductResponse> searchProductsByBrandAndType(Integer brandId, Integer typeId) {
        log.info("Searching product(s) by brandId {}, and typeId:{}", brandId,typeId);
        List<Product> products = productRepository.searchByBrandAndType(brandId, typeId);
        List<ProductResponse> productResponses = products.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
        log.info("Fetched all products");
        return productResponses;
    }

    @Override
    public List<ProductResponse> searchProductsByBrandTypeAndName(Integer brandId, Integer typeId, String keyword) {
        log.info("Searching product(s) by brandId {}, typeId:{} and keyword", brandId,typeId, keyword);
        List<Product> products = productRepository.searchByBrandTypeAndName(brandId, typeId, keyword);
        List<ProductResponse> productResponses = products.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
        log.info("Fetched all products");
        return productResponses;
    }

    @Override
    public ProductResponse createProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        return convertToProductResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(Integer productId, Product product) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        // Mettre à jour les champs du produit existant
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setPictureUrl(product.getPictureUrl());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setType(product.getType());

        Product updatedProduct = productRepository.save(existingProduct);  // Sauvegarde la mise à jour
        return convertToProductResponse(updatedProduct);  // Retourne le produit mis à jour
    }


    @Override
    public void deleteProduct(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    private ProductResponse convertToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .pictureUrl(product.getPictureUrl())
                .productType(product.getType() != null ? product.getType().getName() : "Unknown") // Handle null safely
                .productBrand(product.getBrand() != null ? product.getBrand().getName() : "Unknown") // Handle null safely
                .productQuantity(product.getQunatity())
                .build();
    }

    @Override
    public void incrementQuantity(Integer productId, Integer amount) {
        log.info("Incrementing product {} quantity by {}", productId, amount);
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product not found");
        }
        productRepository.incrementProductQuantity(productId, amount);
    }

    @Override
    public void decrementQuantity(Integer productId, Integer amount) {
        log.info("Decrementing product {} quantity by {}", productId, amount);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (product.getQunatity() < amount) {
            throw new IllegalArgumentException("Not enough quantity in stock");
        }
        productRepository.decrementProductQuantity(productId, amount);
    }



}
