package com.ecomerce.sportscenter.repository;

import com.ecomerce.sportscenter.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedbackRepository  extends JpaRepository<Feedback, Long> {

    List<Feedback> findByRate(Integer rate);

    @Query("SELECT AVG(f.rate) FROM Feedback f")
    Double calculateAverageRating();

    List<Feedback> findByProductId(Long productId);


}