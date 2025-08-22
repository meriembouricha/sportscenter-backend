package com.ecomerce.sportscenter.model;

import java.util.List;

public class RecommendationResponse {
    private List<Integer> recommendations;

    public List<Integer> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Integer> recommendations) {
        this.recommendations = recommendations;
    }
}