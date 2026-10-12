package com.jabai.campustrack.Services;

import com.jabai.campustrack.DTOs.Requests.CreateBuildingRequestDto;
import com.jabai.campustrack.DTOs.Requests.UpdateBuildingRequestDto;
import com.jabai.campustrack.DTOs.Responses.BuildingResponseDto;
import com.jabai.campustrack.Exceptions.CustomExceptions.RowNotFoundException;
import com.jabai.campustrack.Models.Building;
import com.jabai.campustrack.Repositories.BuildingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

import com.jabai.campustrack.DTOs.Requests.SearchBuildingRequestDto;
import com.jabai.campustrack.Repositories.Specifications.BuildingSpecifications;

import java.time.LocalDate;
import java.util.List;

@Service
public class BuildingService {
    private final BuildingRepository buildingRepository;

    public BuildingService(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    // ===== Main operations =====
    // Create
    public BuildingResponseDto createBuilding(CreateBuildingRequestDto request) {
        String name = request.getName();

        Building building = new Building(name);
        Building savedBuilding = buildingRepository.save(building);

        return buildBuildingResponseDto(savedBuilding);
    }

    // Read all
    public Page<BuildingResponseDto> getAllBuildings(Pageable pageable) {
        return buildingRepository
                .findAll(pageable)
                .map(this::buildBuildingResponseDto);
    }

    // Read
    public BuildingResponseDto getBuildingById(Long id) {
        Building building = buildingRepository
                .findById(id)
                .orElseThrow(() -> new RowNotFoundException(String.format("Unable to find building with an ID of %d.", id)));

        return buildBuildingResponseDto(building);
    }

    // Update
    public BuildingResponseDto updateBuilding(Long id, UpdateBuildingRequestDto request) {
        String name = request.getName();

        Building foundBuilding = buildingRepository
                .findById(id)
                .orElseThrow(() -> new RowNotFoundException(String.format("Unable to find building with an ID of %d.", id)));
        foundBuilding.setName(name);
        Building updatedBuilding = buildingRepository.save(foundBuilding);

        return buildBuildingResponseDto(updatedBuilding);
    }

    // Delete
    public void deleteBuilding(Long id) {
        Building building = buildingRepository
                .findById(id)
                .orElseThrow(() -> new RowNotFoundException(String.format("Unable to find building with an ID of %d.", id)));
        buildingRepository.delete(building);
    }

    // Search
    public Page<BuildingResponseDto> searchBuildings(
            SearchBuildingRequestDto request,
            Pageable pageable
    ) {
        Specification<Building> specification = (root, query, cb) -> cb.conjunction();

        if (request.getFrom() != null) {specification = specification.and(BuildingSpecifications.createdAtFrom(request.getFrom()));
        }

        if (request.getTo() != null) {specification = specification.and(BuildingSpecifications.createdAtTo(request.getTo()));
        }

        if (request.getName() != null && !request.getName().isBlank()) {specification = specification.and(BuildingSpecifications.nameContains(request.getName()));
        }

        return buildingRepository
                .findAll(specification, pageable)
                .map(this::buildBuildingResponseDto);
    }

    // ===== Service Utils =====
    private BuildingResponseDto buildBuildingResponseDto(Building building) {
        return new BuildingResponseDto(
                building.getId(),
                building.getName(),
                building.getCreatedAt()
        );
    }
}