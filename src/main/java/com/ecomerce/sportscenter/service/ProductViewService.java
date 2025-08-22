package com.ecomerce.sportscenter.service;

import com.ecomerce.sportscenter.entity.AppUser;
import com.ecomerce.sportscenter.entity.Product;
import com.ecomerce.sportscenter.entity.ProductView;
import com.ecomerce.sportscenter.repository.ProductViewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductViewService {

    private final ProductViewRepository productViewRepository;

    public ProductView saveProductView(AppUser user, Product product) {
        ProductView productView = ProductView.builder()
                .user(user)
                .product(product)
                .viewedAt(LocalDateTime.now())
                .build();

        return productViewRepository.save(productView);
    }

    public List<ProductView> getAllViews() {
        return productViewRepository.findAll();
    }

    public List<ProductView> getViewsByUser(AppUser user) {
        // You can create a custom query in the repository for this
        // e.g., findByUser
        return productViewRepository.findAll() // replace this with actual method
                .stream()
                .filter(view -> view.getUser().getId().equals(user.getId()))
                .toList();
    }

    public void deleteView(Long id) {
        productViewRepository.deleteById(id);
    }
}
