package com.vietlong.sandbox.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.vietlong.sandbox.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

  private static final String CACHE_NAME = "users";
  private static final long SIMULATED_DELAY_MS = 5000L;

  @Cacheable(value = CACHE_NAME, key = "#id")
  public User getUserById(Long id) {
    log.info("Fetching user by id: {}", id);
    simulateSlowDatabase();
    return new User(id, "user " + id, "user " + id + "@example.com");
  }

  @CachePut(value = CACHE_NAME, key = "#user.id")
  public User upsertUser(User user) {
    log.info("Upserting user: {}", user);
    simulateSlowDatabase();
    return user;
  }

  @CacheEvict(value = CACHE_NAME, key = "#id")
  public void deleteUserById(Long id) {
    log.info("Deleting user by id: {}", id);
    simulateSlowDatabase();
  }

  private void simulateSlowDatabase() {
    try {
      log.debug("Simulating slow database operation...");
      Thread.sleep(SIMULATED_DELAY_MS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.error("Database operation interrupted", e);
    }
  }
}
