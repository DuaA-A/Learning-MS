package com.learningsystem.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.learningsystem.entity.Role;
import com.learningsystem.entity.User;

@Service
public class CustomUserService {

    private final Map<String, User> users; // In-memory storage for users (keyed by email)

    public CustomUserService(Map<String, User> users) {
        this.users = users;
    }

    public User findByEmail(String email) {
        User user = users.get(email);
        if (user == null) 
            throw new RuntimeException("User not found with email: " + email);
        return user;
    }

    public User registerUser(String email, String username, String password, Role role) {
        if (users.containsKey(email))
            throw new RuntimeException("User already exists with email: " + email);
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(username);
        newUser.setPassword(password); 
        newUser.setRole(role);
        users.put(email, newUser);
        return newUser;
    }

    public boolean authenticate(String email, String password) {
        User user = users.get(email);
        if (user == null || !user.getPassword().equals(password))
            return false; 
        return true;
    }

    public Map<String, User> getAllUsers() {
        return users;
    }
}
