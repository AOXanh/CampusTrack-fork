package com.jabai.campustrack.DTOs.Responses;

import com.jabai.campustrack.Models.Enums.UserRole;

public class UserProfileResponseDto {
    private final Long id;
    private final String name;
    private final String email;
    private final UserRole UserRole;

    public UserProfileResponseDto(Long id, String name, String email, UserRole userRole){
        this.id = id;
        this.name = name;
        this.email = email; 
        this.UserRole = userRole; 
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public UserRole getUserRole() { return UserRole; }
}
