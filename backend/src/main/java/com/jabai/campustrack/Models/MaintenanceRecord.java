package com.jabai.campustrack.Models;

import com.jabai.campustrack.Models.Enums.MaintenanceRecordStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.Generated;

import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_records")
public class MaintenanceRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "created_at", updatable = false)
  @Generated
  private LocalDateTime createdAt;

  // Core columns
  @Column(name = "completed_at")
  private LocalDateTime completedAt;

  // Incident
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "incident_id", nullable = false)
  private Incident incident;

  // User
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "action", nullable = false, length = 100)
  private String action;

  @Column(name = "remarks", columnDefinition = "TEXT")
  private String remarks;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private MaintenanceRecordStatus status;

  protected MaintenanceRecord() { }

  public MaintenanceRecord(
          Incident incident,
          User user,
          String action,
          String remarks,
          MaintenanceRecordStatus status
  ) {
    this.incident = incident;
    this.user = user;
    this.action = action;
    this.remarks = remarks;
    this.status = status;
    this.completedAt = null;
  }

  public MaintenanceRecord(
          Incident incident,
          User user,
          String action,
          String remarks,
          MaintenanceRecordStatus status,
          LocalDateTime completedAt
  ) {
    this.incident = incident;
    this.user = user;
    this.action = action;
    this.remarks = remarks;
    this.status = status;
    this.completedAt = completedAt;
  }

  // Getters
  public long getId() { return id; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public LocalDateTime getCompletedAt() { return completedAt; }
  public Incident getIncident() { return incident; }
  public User getUser() { return user; }
  public String getAction() { return action; }
  public String getRemarks() { return remarks; }
  public MaintenanceRecordStatus getStatus() { return status; }

  // Setters
  public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
  public void setIncident(Incident incident) { this.incident = incident; }
  public void setUser(User user) { this.user = user; }
  public void setAction(String action) { this.action = action; }
  public void setRemarks(String remarks) { this.remarks = remarks; }
  public void setStatus(MaintenanceRecordStatus status) { this.status = status; }
}
