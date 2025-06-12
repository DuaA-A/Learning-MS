package com.learningsystem.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// import com.learningsystem.dto.UserProfileDTO;
// import com.learningsystem.dto.UserRegistrationDTO;
import com.learningsystem.entity.User;
import com.learningsystem.exeption.UserNotFoundException;
import com.learningsystem.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User userRequest) {
        User user = userService.registerUser(
                userRequest.getUsername(),
                userRequest.getEmail(),
                userRequest.getPassword(),
                userRequest.getRole()
        );
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Principal principal) {
        if (principal == null || principal.getName() == null) 
            throw new UserNotFoundException("Principal not found");
        String username = principal.getName();
        User user = userService.findByName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(
            Principal principal,@RequestBody User userRequest) {
        if (principal == null || principal.getName() == null) {
            throw new UserNotFoundException("Principal not found");
        }

        String username = principal.getName();
        User user = userService.findByName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        User updatedUser = userService.updateUserProfile(
                user.getId(),
                userRequest.getUsername(),
                userRequest.getEmail()
        );

        if (updatedUser == null) 
            throw new RuntimeException("User profile could not be updated. Please try again.");

        return ResponseEntity.ok(updatedUser);
    }
}
