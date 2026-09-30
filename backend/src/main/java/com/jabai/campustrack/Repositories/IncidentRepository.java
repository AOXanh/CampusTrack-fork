package com.jabai.campustrack.Repositories;

import com.jabai.campustrack.Models.Incident;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findAllBy(Pageable pageable);
    List<Incident> findByReportedById(long userId, Pageable pageable);
    List<Incident> findByReportedByIdOrAssignedToId(long reporterId, long workerId, Pageable pageable);

    // Serialize edits, deletion, and lifecycle transitions for the same incident.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Incident i where i.id = :id")
    Optional<Incident> findForUpdate(long id);
}
