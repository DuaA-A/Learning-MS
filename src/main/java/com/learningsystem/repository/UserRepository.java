package com.learningsystem.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.learningsystem.entity.Role;
import com.learningsystem.entity.User;

import jakarta.annotation.PostConstruct;

@Repository
public class UserRepository {
    private final static Map<Long, User> userStore = new ConcurrentHashMap<>();
        private static long currentId = 1;
                
                @PostConstruct
                public void initializeUsers() {
            
                    User instructor = new User(currentId++, "instructor", "instructor@example.com", 
                                                "instructorpass", Role.INSTRUCTOR);
                    instructor.setPassword(new BCryptPasswordEncoder().encode(instructor.getPassword()));
                    userStore.put(instructor.getId(), instructor);
            
                    User student = new User(currentId++, "student", "raghadabsi202@gmail.com", 
                                            "studentpass", Role.STUDENT);
                    student.setPassword(new BCryptPasswordEncoder().encode(student.getPassword()));
                    userStore.put(student.getId(), student);
            
                    User admin = new User(currentId++, "admin", "admin@example.com", 
                                            "adminpass", Role.ADMIN);
                    admin.setPassword(new BCryptPasswordEncoder().encode(admin.getPassword()));
                    userStore.put(admin.getId(), admin);
                }
                public static User save(User user) {
                    if (user.getId() == null) 
                        user.setId(currentId++);
            userStore.put(user.getId(), user);
            System.out.println("All Users: " + userStore);
            return user;
        }
    
        public static Optional<User> findById(Long id) {
            return Optional.ofNullable(userStore.get(id));
    }

    public Optional<User> findByUsername(String username) {
        return userStore.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst(); 
    }
    
    public Optional<User> findByEmail(String email) {
        return userStore.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public List<User> findAll() {
        return new ArrayList<>(userStore.values());
    }

    public void deleteById(Long id) {
        userStore.remove(id);
    }
}
