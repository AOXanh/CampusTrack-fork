package com.jabai.campustrack.DTOs.Responses;

import com.jabai.campustrack.Models.Enums.UserRole;

import java.time.LocalDateTime;

public class UserProfileResponseDto {
    private final LocalDateTime createdAt;
    private final Long id;
    private final String name;
    private final String email;
    private final UserRole userRole;

    public UserProfileResponseDto(LocalDateTime createdAt, Long id, String name, String email, UserRole userRole) {
        this.createdAt = createdAt;
        this.id = id;
        this.name = name;
        this.email = email;
        this.userRole = userRole;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public UserRole getUserRole() { return userRole; }
}
