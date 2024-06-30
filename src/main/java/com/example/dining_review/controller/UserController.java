package com.example.dining_review.controller;

import com.example.dining_review.model.User;
import com.example.dining_review.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@RequestMapping("/users")
@RestController
public class UserController {
  private final UserRepository userRepository;

  public UserController(final UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createUser(@RequestBody User user) {
    Optional<User> existingUser = userRepository.findUserByUsername(user.getUsername());
    if (existingUser.isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }

    userRepository.save(user);
  }

  @GetMapping("/{username}")
  public User getUser(@PathVariable String username) {
    validateUsername(username);

    Optional<User> existingUserOptional = userRepository.findUserByUsername(username);
    if (!existingUserOptional.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    User existingUser = existingUserOptional.get();

    return existingUser;
  }

  @PutMapping("/{username}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateUser(@PathVariable String username, @RequestBody User updatedUser) {
    validateUsername(username);

    Optional<User> existingUserOptional = userRepository.findUserByUsername(username);
    if (!existingUserOptional.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    User existingUser = existingUserOptional.get();

    if (!ObjectUtils.isEmpty(updatedUser.getCity())) {
      existingUser.setCity(updatedUser.getCity());
    }
    if (!ObjectUtils.isEmpty(updatedUser.getState())) {
      existingUser.setState(updatedUser.getState());
    }
    if (!ObjectUtils.isEmpty(updatedUser.getZipCode())) {
      existingUser.setZipCode(updatedUser.getZipCode());
    }
    if (!ObjectUtils.isEmpty(updatedUser.getPeanutAllergy())) {
      existingUser.setPeanutAllergy(updatedUser.getPeanutAllergy());
    }
    if (!ObjectUtils.isEmpty(updatedUser.getEggAllergy())) {
      existingUser.setEggAllergy(updatedUser.getEggAllergy());
    }
    if (!ObjectUtils.isEmpty(updatedUser.getDairyAllergy())) {
      existingUser.setDairyAllergy(updatedUser.getDairyAllergy());
    }

    userRepository.save(existingUser);
  }

  private void validateUsername(String username) {
    if (ObjectUtils.isEmpty(username)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }
}
