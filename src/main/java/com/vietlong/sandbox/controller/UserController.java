package com.vietlong.sandbox.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vietlong.sandbox.model.User;
import com.vietlong.sandbox.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

  private final UserService userService;

  @GetMapping("/{id}")
  @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique identifier. Uses caching for improved performance.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User found"),
  })
  public ResponseEntity<User> getUser(
      @PathVariable Long id) {
    User user = userService.getUserById(id);
    return ResponseEntity.ok(user);
  }

  @PostMapping
  @Operation(summary = "Create or update user", description = "Creates a new user or updates an existing one. The user ID is used to determine if it's an insert or update operation.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User successfully created or updated"),
  })
  public ResponseEntity<User> upsertUser(
      @RequestBody User user) {
    User savedUser = userService.upsertUser(user);
    return ResponseEntity.ok(savedUser);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete user by ID", description = "Deletes a user by their unique identifier. Also removes the user from cache.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "User successfully deleted"),
  })
  public ResponseEntity<Void> deleteUser(
      @PathVariable Long id) {
    userService.deleteUserById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
