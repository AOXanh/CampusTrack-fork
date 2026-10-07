package com.jabai.campustrack.Repositories;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jabai.campustrack.Models.Room;
import com.jabai.campustrack.Models.Enums.RoomCriticality;
import com.jabai.campustrack.Models.Enums.RoomType;

import java.util.Optional;

@NullMarked
@Repository 
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room>{
  @Override
  @EntityGraph(attributePaths = "building")
  Optional<Room> findById(Long id);

  @Override
  @EntityGraph(attributePaths = "building")
  Page<Room> findAll(Pageable pageable);

  
}
