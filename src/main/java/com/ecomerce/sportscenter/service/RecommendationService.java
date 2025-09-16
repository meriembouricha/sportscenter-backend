package com.ecomerce.sportscenter.service;

import com.ecomerce.sportscenter.model.ProductResponse;
import com.ecomerce.sportscenter.model.RecommendationResponse;
import com.ecomerce.sportscenter.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;

@Service
public class RecommendationService {

    private final RestTemplate restTemplate;
    private final ProductService productService;
    private final String recommendationBaseUrl;


    @Autowired
    public RecommendationService(RestTemplate restTemplate, ProductService productService, @Value("${recommendation.service.url}") String recommendationBaseUrl) {
        this.restTemplate = restTemplate;
        this.productService = productService;
        this.recommendationBaseUrl = recommendationBaseUrl;
    }

    public List<ProductResponse> fetchRecommendedProducts(Long userId) {
        String url = recommendationBaseUrl + "/" + userId;

        try {
            RecommendationResponse response = restTemplate.getForObject(url, RecommendationResponse.class);

            if (response != null && response.getRecommendations() != null) {
                return response.getRecommendations().stream()
                        .map(productId -> productService.getProductById(productId))
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            // Log l'erreur mais ne bloque pas l'affichage des produits
            System.out.println("Recommendation service unavailable for user {}: {}"+ userId+ e.getMessage());
        }

        // Retourne une liste vide si le service de recommandations échoue
        return List.of();
    }

}
