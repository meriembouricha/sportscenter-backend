package com.ecomerce.sportscenter.service;

import com.ecomerce.sportscenter.entity.Feedback;
import com.ecomerce.sportscenter.entity.FeedbackStatistics;
import com.ecomerce.sportscenter.repository.FeedbackRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor

public class FeedbackImpl implements IFeedback{

    private final FeedbackRepository feedbackRepo ;

    @Override
    public Feedback addFeedback(Feedback feedback) {
        return feedbackRepo.save(feedback);
    }


    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepo.findAll();
    }

    @Override
    public void deleteFeedback(Long id) {
        feedbackRepo.deleteById(id);
    }

    @Override
    public List<Feedback> getFeedbacksByRate(Integer rate) {
        return feedbackRepo.findByRate(rate);
    }

    @Override
    public long getTotalFeedbacks() {
        return feedbackRepo.count();
    }

    @Override
    public FeedbackStatistics calculateStatistics() {
        FeedbackStatistics statistics = new FeedbackStatistics();
        statistics.setAverageRating(feedbackRepo.calculateAverageRating());
        statistics.setTotalFeedbacks(feedbackRepo.count());
        return statistics;
    }

    @Override
    public List<Feedback> getFeedbacksByProductId(Long productId) {
        return feedbackRepo.findByProductId(productId); // Implémentez cette méthode
    }




}