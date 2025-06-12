package com.learningsystem.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.learningsystem.entity.Role;
import com.learningsystem.entity.User;
import com.learningsystem.repository.UserRepository;

@Service
public class UserService {

    private final AtomicLong currentId = new AtomicLong(4); 

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(String username, String email, String password, Role role) {
        if (userRepository.findByUsername(username).isPresent()) 
            throw new RuntimeException("Username already exists!");
        if (userRepository.findByEmail(email).isPresent()) 
            throw new RuntimeException("Email already exists!");
        User user = new User();
        user.setId(currentId.getAndIncrement());
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); 
        user.setRole(role);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findByName(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUserProfile(Long id, String username, String email) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        if (username != null && !username.trim().isEmpty()) 
            user.setUsername(username);
        if (email != null && !email.trim().isEmpty()) {
            Optional<User> existingUserWithEmail = userRepository.findByEmail(email);
            if (existingUserWithEmail.isPresent() && !existingUserWithEmail.get().getId().equals(id)) 
                throw new RuntimeException("Email already in use by another user!");
            user.setEmail(email);
        }
        return userRepository.save(user); 
    }
}
