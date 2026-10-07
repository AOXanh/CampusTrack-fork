package com.jabai.campustrack.Repositories.Specifications;

import com.jabai.campustrack.Models.Enums.NfcTagStatus;
import com.jabai.campustrack.Models.NfcTag;
import org.springframework.data.jpa.domain.Specification;

public class NfcTagSpecifications {
  public static Specification<NfcTag> hasAssetId(Long assetId) {
    return (root, query, criteriaBuilder) -> {
      if (assetId == null)
        return null;
      return criteriaBuilder.equal(root.get("asset").get("id"), assetId);
    };
  }

  public static Specification<NfcTag> hasUid(String uid) {
    return (root, query, criteriaBuilder) -> {
      if (uid == null)
        return null;
      return criteriaBuilder.equal(root.get("uid"), uid);
    };
  }

  public static Specification<NfcTag> hasStatus(NfcTagStatus status) {
    return (root, query, criteriaBuilder) -> {
      if (status == null)
        return null;
      return criteriaBuilder.equal(root.get("status"), status);
    };
  }
}
