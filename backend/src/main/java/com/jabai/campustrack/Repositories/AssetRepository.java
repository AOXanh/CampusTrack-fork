package com.jabai.campustrack.Repositories;

import com.jabai.campustrack.Models.Asset;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@NullMarked
@Repository
public interface AssetRepository extends JpaRepository<Asset, Long>, JpaSpecificationExecutor<Asset> {
  @Override
  @EntityGraph(attributePaths = {"room"})
  Optional<Asset> findById(Long id);

  @Override
  @EntityGraph(attributePaths = {"room"})
  Page<Asset> findAll(Pageable pageable);

  // fetch the room together with the assets so search results don't trigger extra queries
  @Override
  @EntityGraph(attributePaths = {"room"})
  Page<Asset> findAll(@Nullable Specification<Asset> spec, Pageable pageable);
}