package com.jabai.campustrack.DTOs.Responses;

import java.util.List;

import org.springframework.data.domain.Page;

import com.jabai.campustrack.Repositories.RoomJson;

public class GetRoomsResponseDto {
   private final Page<RoomJson> rooms;

   public GetRoomsResponseDto(Page<RoomJson> rooms) {
      this.rooms = rooms;
   }

   public Page<RoomJson> getRooms() {
       return rooms;
   }
}
