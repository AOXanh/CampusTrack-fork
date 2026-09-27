package com.jabai.campustrack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jabai.campustrack.model.*;

@Repository 
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);
     
}
