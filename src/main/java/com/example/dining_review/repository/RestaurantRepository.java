package com.example.dining_review.repository;

import com.example.dining_review.model.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.List;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {
  Optional<Restaurant> findRestaurantsByNameAndZipCode(String name, String zipCode);
  List<Restaurant> findRestaurantsByZipCodeAndPeanutScoreNotNullOrderByPeanutScore(String zipCode);
  List<Restaurant> findRestaurantsByZipCodeAndEggScoreNotNullOrderByEggScore(String zipCode);
  List<Restaurant> findRestaurantsByZipCodeAndDairyScoreNotNullOrderByDairyScore(String zipCode);
}
