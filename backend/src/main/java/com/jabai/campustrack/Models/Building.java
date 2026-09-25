package com.jabai.campustrack.Models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Buildings")
public class Building {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "created_at", updatable = false, insertable = false)
  private LocalDateTime createdAt;

  // Core columns
  // Rooms
  @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
  private List<Room> rooms = new ArrayList<>();

  @Column(name = "name", nullable = false)
  private String name;

  // Getters
  public List<Room> getRooms() { return rooms; }
  public long getId() { return id; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public String getName() { return name; }

  // Setters
  public void setName(String value) { name = value; }
  public void addRoom(Room value) {
    rooms.add(value);
    value.setBuilding(this);
  }
  public void removeRoom(Room value) {
    rooms.remove(value);
  }
}
