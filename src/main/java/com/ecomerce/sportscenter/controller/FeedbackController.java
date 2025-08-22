package com.ecomerce.sportscenter.controller;

import com.ecomerce.sportscenter.entity.Feedback;
import com.ecomerce.sportscenter.service.IFeedback;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
@CrossOrigin(origins = "http://localhost:4200")
public class FeedbackController {

    private final IFeedback feedbackService;

    public FeedbackController(IFeedback feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<Feedback> addFeedback(@RequestBody Feedback feedback) {
        Feedback savedFeedback = feedbackService.addFeedback(feedback);
        return ResponseEntity.ok(savedFeedback);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByProductId(@PathVariable Long productId) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksByProductId(productId);
        return ResponseEntity.ok(feedbacks);
    }
}