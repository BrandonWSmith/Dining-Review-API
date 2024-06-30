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
public class Restaurant {
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private String address;
  private String city;
  private String state;
  private String zipCode;

  private String phoneNumber;
  private String website;

  private String overallScore;
  private String peanutScore;
  private String eggScore;
  private String dairyScore;
}
