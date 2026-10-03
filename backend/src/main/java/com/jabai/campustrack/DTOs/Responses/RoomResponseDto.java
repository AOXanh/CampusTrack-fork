package com.jabai.campustrack.DTOs.Responses;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.jabai.campustrack.Repositories.RoomJson;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomResponseDto {

    private final String message;

    @JsonUnwrapped
    private final PagedModel<RoomJson> rooms;

    public RoomResponseDto(String message) {
        this.message = message;
        this.rooms = null;
    }

    public RoomResponseDto(Page<RoomJson> rooms) {
        this.rooms = new PagedModel<>(rooms);
        this.message = null;
    }

    public String getMessage() {
        return message;
    }
}
