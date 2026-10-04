package com.jabai.campustrack.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jabai.campustrack.Models.Room;

@Repository 
public interface RoomRepository extends JpaRepository<Room, Long> { }
