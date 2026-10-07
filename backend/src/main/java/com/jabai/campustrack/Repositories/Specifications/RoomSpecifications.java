package com.jabai.campustrack.Repositories.Specifications;

import org.springframework.data.jpa.domain.Specification;

import com.jabai.campustrack.Models.Room;
import com.jabai.campustrack.Models.Enums.RoomCriticality;
import com.jabai.campustrack.Models.Enums.RoomType;

public class RoomSpecifications {

    public static Specification<Room> hasBuilding(Long buildingId) {
        return (root, query, cb) ->
            buildingId == null ? null : cb.equal(root.get("building").get("id"), buildingId);
    }

    public static Specification<Room> hasCriticality(RoomCriticality criticality) {
        return (root, query, cb) ->
            criticality == null ? null : cb.equal(root.get("criticality"), criticality);
    }

    public static Specification<Room> hasRoomType(RoomType roomType) {
        return (root, query, cb) ->
            roomType == null ? null : cb.equal(root.get("roomType"), roomType);
    }

    public static Specification<Room> hasRoomNumber(String roomNumber) {
        return (root, query, cb) ->
            roomNumber == null ? null : cb.equal(root.get("roomNumber"), roomNumber);
    }

    public static Specification<Room> hasCapacity(Integer capacity) {
        return (root, query, cb) ->
            capacity == null ? null : cb.equal(root.get("capacity"), capacity);
    }
}