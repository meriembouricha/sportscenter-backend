package com.ecomerce.sportscenter.controller;

import com.ecomerce.sportscenter.entity.AppUser;
import com.ecomerce.sportscenter.entity.Product;
import com.ecomerce.sportscenter.entity.ProductView;
import com.ecomerce.sportscenter.model.ProductViewRequest;
import com.ecomerce.sportscenter.service.ProductViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-views")
@RequiredArgsConstructor
public class ProductViewController {

    private final ProductViewService productViewService;

    @PostMapping
    public ResponseEntity<ProductView> createProductView(@RequestBody ProductViewRequest request) {
        // Normally you would validate and load entities from DB, here it's simplified
        AppUser user = request.getUser();
        Product product = request.getProduct();

        ProductView createdView = productViewService.saveProductView(user, product);
        return ResponseEntity.ok(createdView);
    }

    @GetMapping
    public ResponseEntity<List<ProductView>> getAllViews() {
        return ResponseEntity.ok(productViewService.getAllViews());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteView(@PathVariable Long id) {
        productViewService.deleteView(id);
        return ResponseEntity.noContent().build();
    }
}
