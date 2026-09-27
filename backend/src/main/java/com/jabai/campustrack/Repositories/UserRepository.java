package com.jabai.campustrack.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jabai.campustrack.Models.*;

@Repository 
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
}
