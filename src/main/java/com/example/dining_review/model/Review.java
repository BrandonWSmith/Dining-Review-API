package com.example.dining_review.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Review {
  @Id
  @GeneratedValue
  private Long id;

  private String submittedBy;

  private Long restaurantId;

  private Integer peanutScore;
  private Integer eggScore;
  private Integer dairyScore;

  private String comment;

  private ReviewStatus reviewStatus;
}
