package com.example.dining_review.repository;

import com.example.dining_review.model.Review;
import com.example.dining_review.model.ReviewStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {
  List<Review> findReviewsByStatus(ReviewStatus reviewStatus);
  List<Review> findReviewsByRestaurantIdAndStatus(Long restaurantId, ReviewStatus reviewStatus);
}
