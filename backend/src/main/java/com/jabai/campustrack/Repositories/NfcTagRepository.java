package com.jabai.campustrack.Repositories;

import com.jabai.campustrack.Models.Enums.NfcTagStatus;
import com.jabai.campustrack.Models.NfcTag;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@NullMarked
@Repository
public interface NfcTagRepository extends JpaRepository<NfcTag, Long>, JpaSpecificationExecutor<NfcTag> {
  @Override
  @EntityGraph(attributePaths = {"asset", "asset.room"})
  Optional<NfcTag> findById(Long id);

  @Override
  @EntityGraph(attributePaths = {"asset", "asset.room"})
  Page<NfcTag> findAll(Pageable pageable);

  boolean existsByUid(String uid);
}
