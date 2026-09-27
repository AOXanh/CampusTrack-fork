package com.jabai.campustrack.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.jabai.campustrack.model.Enums.Criticality;
import com.jabai.campustrack.model.Enums.RoomType;

@Entity
@Table(name = "Rooms")
public class Room {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "created_at", updatable = false, insertable = false)
  private LocalDateTime createdAt;

  // Core columns
  // Building
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "building_id", nullable = false)
  private Building building;

  @Column(name = "room_number", nullable = false)
  private int roomNumber;

  @Column(name = "capacity", nullable = false)
  private int capacity;

  @Enumerated(EnumType.STRING)
  @Column(name = "room_type", nullable = false)
  private RoomType roomType;

  @Enumerated(EnumType.STRING)
  @Column(name = "criticality", nullable = false)
  private Criticality criticality;

  // Getters
  
  
  public Building getBuilding() { return building; }
  public int getRoomNumber() { return roomNumber; }
  public int getCapacity() { return capacity; }
  public RoomType getRoomType() { return roomType; }
  public Criticality getCriticality() { return criticality; }

  // Setters
  public void setBuilding(Building value) { building = value; }
  public void setRoomNumber(int value) { roomNumber = value; }
  public void setCapacity(int value) { capacity = value; }
  public void setRoomType(RoomType value) { roomType = value; }
  public void setCriticality(Criticality value) { criticality = value; }
  
}
