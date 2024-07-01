package com.example.dining_review.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name="users")
@Getter
@Setter
@RequiredArgsConstructor
public class User {
  @Id
  @GeneratedValue
  private Long id;

  private String username;

  private String city;
  private String state;
  private String zipCode;

  private Boolean peanutAllergy;
  private Boolean eggAllergy;
  private Boolean dairyAllergy;
}
