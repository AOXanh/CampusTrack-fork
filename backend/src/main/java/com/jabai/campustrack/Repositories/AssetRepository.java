package com.jabai.campustrack.Repositories;

import com.jabai.campustrack.Models.Asset;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@NullMarked
@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
  @Override
  @EntityGraph(attributePaths = {"room"})
  Optional<Asset> findById(Long id);

  @Override
  @EntityGraph(attributePaths = {"room"})
  Page<Asset> findAll(Pageable pageable);
}
