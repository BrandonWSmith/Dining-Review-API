package com.example.dining_review.controller;

import com.example.dining_review.model.Restaurant;
import com.example.dining_review.repository.RestaurantRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.Collections;

@RequestMapping("/restaurants")
@RestController
public class RestaurantController {
  private final RestaurantRepository restaurantRepository;
  private final Pattern zipCodePattern = Pattern.compile("\\d{5}");

  public RestaurantController(final RestaurantRepository restaurantRepository) {
    this.restaurantRepository = restaurantRepository;
  }
  
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createRestaurant(@RequestBody Restaurant restaurant) {
    if (ObjectUtils.isEmpty(restaurant.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    validateZipCode(restaurant.getZipCode());

    Optional<Restaurant> existingRestaurant = restaurantRepository.findRestaurantsByNameAndZipCode(restaurant.getName(), restaurant.getZipCode());
    if (existingRestaurant.isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }

    restaurantRepository.save(restaurant);
  }

  @GetMapping
  public Iterable<Restaurant> getAllRestaurants() {
    return restaurantRepository.findAll();
  }

  @GetMapping("/{id}")
  public Restaurant getRestaurant(@PathVariable Long id) {
    Optional<Restaurant> existingRestaurantOptional = restaurantRepository.findById(id);
    if (!existingRestaurantOptional.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    Restaurant existingRestaurant = existingRestaurantOptional.get();

    return existingRestaurant;
  }

  @SuppressWarnings("unchecked")
  @GetMapping("/search")
  public Iterable<Restaurant> getRestaurantsWithScore(@RequestParam String zipCode, @RequestParam String allergy) {
    validateZipCode(zipCode);

    Iterable<Restaurant> resataurants = Collections.EMPTY_LIST;
    if (allergy.equalsIgnoreCase("peanut")) {
      resataurants = restaurantRepository.findRestaurantsByZipCodeAndPeanutScoreNotNullOrderByPeanutScore(zipCode);
    }
    else if (allergy.equalsIgnoreCase("egg")) {
      resataurants = restaurantRepository.findRestaurantsByZipCodeAndEggScoreNotNullOrderByEggScore(zipCode);
    }
    else if (allergy.equalsIgnoreCase("dairy")) {
      resataurants = restaurantRepository.findRestaurantsByZipCodeAndDairyScoreNotNullOrderByDairyScore(zipCode);
    }
    else {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    return resataurants;
  }

  private void validateZipCode(String zipCode) {
    if (!zipCodePattern.matcher(zipCode).matches()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }
}
