
package com.learningsystem.entity;

public enum Role {
    ADMIN,
    INSTRUCTOR,
    STUDENT;

    public static Role fromString(String role) {
        switch (role.toUpperCase()) {
            case "ADMIN":
                return ADMIN;
            case "INSTRUCTOR":
                return INSTRUCTOR;
            case "STUDENT":
                return STUDENT;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}