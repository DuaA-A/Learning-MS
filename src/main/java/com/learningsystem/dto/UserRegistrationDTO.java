// package com.learningsystem.dto;

// import com.learningsystem.entity.Role;

// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.Size;

// public class UserRegistrationDTO {
//     @NotBlank(message = "Username is required")
//     @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
//     private String username;

//     @NotBlank(message = "Email is required")
//     @Email(message = "Invalid email format")
//     private String email;

//     @NotBlank(message = "Password is required")
//     @Size(min = 6, message = "Password must be at least 6 characters long")
//     private String password;

//     @NotBlank(message = "Role is required")
//     private Role role; // Updated to use Role enum

//     public UserRegistrationDTO() {}

//     public UserRegistrationDTO(String username, String email, String password, Role role) {
//         this.username = username;
//         this.email = email;
//         this.password = password;
//         this.role = role;
//     }

//     public String getUsername() {
//         return username;
//     }

//     public void setUsername(String username) {
//         this.username = username;
//     }

//     public String getEmail() {
//         return email;
//     }

//     public void setEmail(String email) {
//         this.email = email;
//     }

//     public String getPassword() {
//         return password;
//     }

//     public void setPassword(String password) {
//         this.password = password;
//     }

//     public Role getRole() {
//         return role;
//     }

//     public void setRole(Role role) {
//         this.role = role;
//     }
// }
