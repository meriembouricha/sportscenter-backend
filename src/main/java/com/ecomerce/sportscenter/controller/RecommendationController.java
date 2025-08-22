package com.ecomerce.sportscenter.controller;

import com.ecomerce.sportscenter.model.ProductResponse;
import com.ecomerce.sportscenter.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ProductResponse>> getRecommendations(@PathVariable Long userId) {
        List<ProductResponse> productResponses = recommendationService.fetchRecommendedProducts(userId);
        return ResponseEntity.ok(productResponses);
    }

}
