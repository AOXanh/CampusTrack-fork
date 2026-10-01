package com.jabai.campustrack.DTOs.Responses;

import java.util.List;

import com.jabai.campustrack.Repositories.RoomJson;

public class GetRoomsResponseDto {
   private final List<RoomJson> rooms;

   public GetRoomsResponseDto(List<RoomJson> rooms) {
      this.rooms = rooms;
   }

   public List<RoomJson> getRooms() {
       return rooms;
   }
}
