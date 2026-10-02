package com.jabai.campustrack.DTOs.Responses;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import com.jabai.campustrack.Repositories.RoomJson;

public class GetRoomsResponseDto {
   private final PagedModel<EntityModel<RoomJson>> rooms;

   public GetRoomsResponseDto(PagedModel<EntityModel<RoomJson>> rooms) {
      this.rooms = rooms;
   }

   public PagedModel<EntityModel<RoomJson>> getRooms() {
       return rooms;
   }
}
