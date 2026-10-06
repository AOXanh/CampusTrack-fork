package com.jabai.campustrack.DTOs.Requests;

import org.springframework.data.jpa.domain.Specification;

import com.jabai.campustrack.Models.Enums.RoomCriticality;
import com.jabai.campustrack.Models.Enums.RoomType;

public record SearchRoomsRequestDto(
    Long building_id, 
    String room_number, 
    Integer capacity, 
    RoomType room_type, 
    RoomCriticality criticality) {
    }
