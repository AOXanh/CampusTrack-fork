package com.jabai.campustrack.DTOs.Requests;

import com.jabai.campustrack.Models.Enums.RoomCriticality;
import com.jabai.campustrack.Models.Enums.RoomType;

public class SearchRoomsRequestDto {
    private final Long building_id;
    private final String room_number;
    private final Integer capacity;
    private final RoomType room_type;
    private final RoomCriticality criticality;

    public SearchRoomsRequestDto(Long building_id, String room_number, Integer capacity, RoomType room_type, RoomCriticality criticality) {
        this.building_id = building_id;
        this.room_number = room_number;
        this.capacity = capacity;
        this.room_type = room_type;
        this.criticality = criticality;
    }

    public Long getBuilding_id() {
        return building_id;
    }
    public Integer getCapacity() {
        return capacity;
    }
    public RoomCriticality getCriticality() {
        return criticality;
    }
   public String getRoom_number() {
       return room_number;
   }
   public RoomType getRoom_type() {
       return room_type;
   }
}
