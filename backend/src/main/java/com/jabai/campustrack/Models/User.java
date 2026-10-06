package com.jabai.campustrack.Models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.jabai.campustrack.Models.Enums.UserRole;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "created_at", updatable = false, insertable = false)
  private LocalDateTime createdAt;

  // Core columns
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "email", nullable = false, unique = true, length = 100)
  private String email;

  @Column(name = "password_hash", nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "user_role", nullable = false)
  private UserRole userRole;

  protected User() { }

  public User(String name, String email, String password, UserRole userRole) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.userRole = userRole;
  }

  // Getters
  public Long getId() { return id; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public String getName() { return name; }
  public String getEmail() { return email; }
  public String getHashedPassword() { return password; }
  public UserRole getUserRole() { return userRole; }

  //Setters
  public void setName(String value) { name = value; }
  public void setEmail(String value) { email = value; }
  public void setHashedPassword(String value) { password = value; }
  public void setUserRole(UserRole value) { userRole = value; }
}
