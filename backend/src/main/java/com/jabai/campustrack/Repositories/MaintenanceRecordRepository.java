package com.jabai.campustrack.Repositories;

import com.jabai.campustrack.Models.MaintenanceRecord;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@NullMarked
@Repository
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {
  @Override
  @EntityGraph(attributePaths = {"incident", "user"})
  Optional<MaintenanceRecord> findById(Long id);

  @Override
  @EntityGraph(attributePaths = {"incident", "user"})
  Page<MaintenanceRecord> findAll(Pageable pageable);
}