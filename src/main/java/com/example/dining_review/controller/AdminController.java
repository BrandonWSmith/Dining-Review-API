package com.example.dining_review.controller;

import com.example.dining_review.model.AdminReviewAction;
import com.example.dining_review.model.Review;
import com.example.dining_review.model.ReviewStatus;
import com.example.dining_review.model.Restaurant;
import com.example.dining_review.repository.ReviewRepository;
import com.example.dining_review.repository.RestaurantRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@RequestMapping("/admin")
@RestController
public class AdminController {
  private final ReviewRepository reviewRepository;
  private final RestaurantRepository restaurantRepository;

  private final DecimalFormat decimalFormat = new DecimalFormat("0.00");

  public AdminController(final ReviewRepository reviewRepository, final RestaurantRepository restaurantRepository) {
    this.reviewRepository = reviewRepository;
    this.restaurantRepository = restaurantRepository;
  }

  @GetMapping("/reviews")
  public List<Review> getReviewByStatus(@RequestParam String review_status) {
    ReviewStatus reviewStatus = ReviewStatus.PENDING;
    try {
      reviewStatus = ReviewStatus.valueOf(review_status.toUpperCase());
    }
    catch (IllegalArgumentException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    return reviewRepository.findReviewsByReviewStatus(reviewStatus);
  }

  @PutMapping("/reviews/{id}")
  public void performReviewAction(@PathVariable Long id, @RequestBody AdminReviewAction adminReviewAction) {
    Optional<Review> reviewOptional = reviewRepository.findById(id);
    if (reviewOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    Review review = reviewOptional.get();

    Optional<Restaurant> restauranOptional = restaurantRepository.findById(review.getRestaurantId());
    if (restauranOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    if (adminReviewAction.getAccept()) {
      review.setReviewStatus(ReviewStatus.ACCEPTED);
    }
    else {
      review.setReviewStatus(ReviewStatus.REJECTED);
    }

    reviewRepository.save(review);
    updateRestaurantScores(restauranOptional.get());
  }

  private void updateRestaurantScores(Restaurant restaurant) {
    List<Review> reviews = reviewRepository.findReviewsByRestaurantIdAndReviewStatus(restaurant.getId(), ReviewStatus.ACCEPTED);
    if (reviews.size() == 0) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    int peanutSum = 0;
    int peanutCount = 0;
    int eggSum = 0;
    int eggCount = 0;
    int dairySum = 0;
    int dairyCount = 0;
    for (Review r : reviews) {
      if (!ObjectUtils.isEmpty(r.getPeanutScore())) {
        peanutSum += r.getPeanutScore();
        peanutCount++;
      }
      if (!ObjectUtils.isEmpty(r.getEggScore())) {
        eggSum += r.getEggScore();
        eggCount++;
      }
      if (!ObjectUtils.isEmpty(r.getDairyScore())) {
        dairySum += r.getDairyScore();
        eggCount++;
      }
    }

    int totalCount = peanutCount + eggCount + dairyCount;
    int totalSum = peanutSum + eggSum + dairySum;

    float overallScore = (float) totalSum / totalCount;
    restaurant.setOverallScore(decimalFormat.format(overallScore));

    if (peanutCount > 0) {
      float peanutScore = (float) peanutSum / peanutCount;
      restaurant.setPeanutScore(decimalFormat.format(peanutScore));
    }
    if (eggCount > 0) {
      float eggScore = (float) eggSum / eggCount;
      restaurant.setEggScore(decimalFormat.format(eggScore));
    }
    if (dairyCount > 0) {
      float dairyScore = (float) dairySum / dairyCount;
      restaurant.setDairyScore(decimalFormat.format(dairyScore));
    }

    restaurantRepository.save(restaurant);
  }
}
