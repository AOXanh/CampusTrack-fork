package com.jabai.campustrack.model;

import jakarta.persistence.*;


import java.time.LocalDateTime;

import com.jabai.campustrack.model.Enums.UserRole;

@Entity
@Table(name = "Users") 
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "created_at", updatable = false, insertable = false)
  private LocalDateTime createdAt;

  // Core columns
  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password_hash", nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "user_role", nullable = false)
  private UserRole userRole;

  // Getters 

  
  
  public long getId() { return id; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public String getName() { return name; }
  public String getEmail() { return email; }
  public String getHashedPassword() { return password; }
  public UserRole getUserRole() { return userRole; }

 
 
//Commented the Getters and Setters for the Lombok extension 

  //Setters 
   
  public void setName(String value) { name = value; }
  public void setEmail(String value) { email = value; }
  public void setHashedPassword(String value) { password = value; }
  public void setUserRole(UserRole value) { userRole = value; }

   

  
}
