package com.example.dining_review.controller;

import com.example.dining_review.model.Review;
import com.example.dining_review.model.ReviewStatus;
import com.example.dining_review.model.Restaurant;
import com.example.dining_review.model.User;
import com.example.dining_review.repository.ReviewRepository;
import com.example.dining_review.repository.RestaurantRepository;
import com.example.dining_review.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@RequestMapping("/reviews")
@RestController
public class ReviewController {
  private final ReviewRepository reviewRepository;
  private final RestaurantRepository restaurantRepository;
  private final UserRepository userRepository;

  public ReviewController(final ReviewRepository reviewRepository, final RestaurantRepository restaurantRepository, final UserRepository userRepository) {
    this.reviewRepository = reviewRepository;
    this.restaurantRepository = restaurantRepository;
    this.userRepository = userRepository;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createReview(@RequestBody Review review) {
    if (ObjectUtils.isEmpty(review.getSubmittedBy())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (ObjectUtils.isEmpty(review.getRestaurantId())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (ObjectUtils.isEmpty(review.getPeanutScore()) && ObjectUtils.isEmpty(review.getEggScore()) && ObjectUtils.isEmpty(review.getDairyScore())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    Optional<User> userOptional = userRepository.findUserByUsername(review.getSubmittedBy());
    if (userOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    Optional<Restaurant> restaurantOptional = restaurantRepository.findById(review.getRestaurantId());
    if (restaurantOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    review.setReviewStatus(ReviewStatus.PENDING);
    reviewRepository.save(review);
  }
}
