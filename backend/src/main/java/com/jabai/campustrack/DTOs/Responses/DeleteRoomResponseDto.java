package com.jabai.campustrack.DTOs.Responses;

public class DeleteRoomResponseDto {
    private final String message;
    
    public DeleteRoomResponseDto(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}
