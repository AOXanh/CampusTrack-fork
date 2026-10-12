package com.jabai.campustrack.Repositories.Specifications;

import com.jabai.campustrack.Models.Asset;
import com.jabai.campustrack.Models.Enums.AssetCategory;
import com.jabai.campustrack.Models.Enums.AssetCondition;
import com.jabai.campustrack.Models.Enums.AssetCriticality;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class AssetSpecifications {

    // exact match on room id
    public static Specification<Asset> hasRoomId(Long roomId) {
        return (root, query, cb) -> {
            if (roomId == null)
                return null;
            return cb.equal(root.get("room").get("id"), roomId);
        };
    }

    // keyword search (case-insensitive, partial match)
    public static Specification<Asset> hasNameKeyword(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank())
                return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.trim().toLowerCase() + "%");
        };
    }

    // keyword search (case-insensitive, partial match)
    public static Specification<Asset> hasBrandKeyword(String brand) {
        return (root, query, cb) -> {
            if (brand == null || brand.isBlank())
                return null;
            return cb.like(cb.lower(root.get("brand")), "%" + brand.trim().toLowerCase() + "%");
        };
    }

    // keyword search (case-insensitive, partial match)
    public static Specification<Asset> hasModelKeyword(String model) {
        return (root, query, cb) -> {
            if (model == null || model.isBlank())
                return null;
            return cb.like(cb.lower(root.get("model")), "%" + model.trim().toLowerCase() + "%");
        };
    }

    // exact match on serial number
    public static Specification<Asset> hasSerialNumber(String serialNumber) {
        return (root, query, cb) -> {
            if (serialNumber == null || serialNumber.isBlank())
                return null;
            return cb.equal(root.get("serialNumber"), serialNumber.trim());
        };
    }

    // exact match on category enum
    public static Specification<Asset> hasCategory(AssetCategory category) {
        return (root, query, cb) -> {
            if (category == null)
                return null;
            return cb.equal(root.get("category"), category);
        };
    }

    // exact match on condition enum
    public static Specification<Asset> hasCondition(AssetCondition condition) {
        return (root, query, cb) -> {
            if (condition == null)
                return null;
            return cb.equal(root.get("condition"), condition);
        };
    }

    // exact match on criticality enum
    public static Specification<Asset> hasCriticality(AssetCriticality criticality) {
        return (root, query, cb) -> {
            if (criticality == null)
                return null;
            return cb.equal(root.get("criticality"), criticality);
        };
    }

    // created at range, either side can be omitted
    public static Specification<Asset> createdBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            if (from == null && to == null)
                return null;
            if (from == null)
                return cb.lessThanOrEqualTo(root.get("createdAt"), to);
            if (to == null)
                return cb.greaterThanOrEqualTo(root.get("createdAt"), from);
            return cb.between(root.get("createdAt"), from, to);
        };
    }
}