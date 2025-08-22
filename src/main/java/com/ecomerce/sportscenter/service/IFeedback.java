package com.ecomerce.sportscenter.service;



import com.ecomerce.sportscenter.entity.Feedback;
import com.ecomerce.sportscenter.entity.FeedbackStatistics;

import java.util.List;

public interface IFeedback {

    Feedback addFeedback (Feedback feedback);

    List<Feedback> getAllFeedbacks();
    void deleteFeedback(Long id);

    List<Feedback> getFeedbacksByRate(Integer rate);

    FeedbackStatistics calculateStatistics();
    long getTotalFeedbacks();
    List<Feedback> getFeedbacksByProductId(Long productId); // Ajoutez cette méthode



}