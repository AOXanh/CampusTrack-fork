package com.jabai.campustrack.DTOs.Responses;

import com.jabai.campustrack.Models.Enums.UserRole;

public class UserProfileResponseDto {
    
    private String name; 
    private String email; 
    private UserRole UserRole; 

    public UserProfileResponseDto(String name, String email, UserRole userRole){ 
        this.name = name;
        this.email = email; 
        this.UserRole = userRole; 
    }

    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public UserRole getUserRole() {
        return UserRole;
    }

}
