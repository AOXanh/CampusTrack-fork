package com.jabai.campustrack.DTOs.Responses;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.jabai.campustrack.Models.Building;
import com.jabai.campustrack.Models.Room;
import com.jabai.campustrack.Models.Enums.RoomCriticality;
import com.jabai.campustrack.Models.Enums.RoomType;
import com.jabai.campustrack.Repositories.RoomJson;

public class GetRoomResponseDto {
   private final long id;
   private final LocalDateTime createdAt;
   private final Map<String, Object> building;
   private final String roomNumber;
   private final long capacity;
   private final RoomType roomType;
   private final RoomCriticality criticality;

   public GetRoomResponseDto(long id, LocalDateTime createdAt, Map<String, Object> building, String roomNumber, long capacity, RoomType roomType, RoomCriticality criticality) {
    this.id = id;
    this.createdAt = createdAt;
    this.building = building;
    this.roomNumber = roomNumber;
    this.capacity = capacity;
    this.roomType = roomType;
    this.criticality = criticality;
   }

   public Map<String, Object> getBuilding() {
       return building;
   }

   public long getCapacity() {
       return capacity;
   }

   public LocalDateTime getCreatedAt() {
       return createdAt;
   }

   public RoomCriticality getCriticality() {
       return criticality;
   }
   
   public long getId() {
       return id;
   }

   public String getRoomNumber() {
       return roomNumber;
   }

   public RoomType getRoomType() {
       return roomType;
   }
}
