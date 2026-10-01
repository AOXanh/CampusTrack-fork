package com.jabai.campustrack.Repositories;

import java.time.LocalDateTime;
import com.jabai.campustrack.Models.Enums.RoomCriticality;
import com.jabai.campustrack.Models.Enums.RoomType;

public interface RoomJson {
    Long getId();
    Integer getCapacity();
    String getRoomNumber();
    RoomType getRoomType();
    RoomCriticality getCriticality();
    LocalDateTime getCreatedAt();
}