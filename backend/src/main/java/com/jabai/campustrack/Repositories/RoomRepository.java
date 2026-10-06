package com.jabai.campustrack.Repositories;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jabai.campustrack.Models.Room;
import com.jabai.campustrack.Models.Enums.RoomCriticality;
import com.jabai.campustrack.Models.Enums.RoomType;

import java.util.Optional;

@NullMarked
@Repository 
public interface RoomRepository extends JpaRepository<Room, Long> {
  @Override
  @EntityGraph(attributePaths = "building")
  Optional<Room> findById(Long id);

  @Override
  @EntityGraph(attributePaths = "building")
  Page<Room> findAll(Pageable pageable);

    @Query("""
        SELECT r FROM Room r
        WHERE (:buildingId IS NULL OR r.building.id = :buildingId)
          AND (:criticality IS NULL OR r.criticality = :criticality)
          AND (:roomType IS NULL OR r.roomType = :roomType)
          AND (:roomNumber IS NULL OR LOWER(r.roomNumber) LIKE LOWER(CONCAT('%', :roomNumber, '%')))
          AND (:capacity IS NULL OR r.capacity = :capacity)
        """)
    Page<Room> search(@Param("buildingId") Long building_id,
                          @Param("criticality") RoomCriticality criticality,
                          @Param("roomType") RoomType room_type,
                          @Param("roomNumber") String room_number,
                          @Param("capacity") Integer capacity,
                          Pageable pageable);
}
