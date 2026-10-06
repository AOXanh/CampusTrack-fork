package com.jabai.campustrack.DTOs.Requests;

import com.jabai.campustrack.Models.Enums.NfcTagStatus;

public class SearchNfcTagRequestDto {
  private final Long assetId;
  private final String uid;
  private final NfcTagStatus status;

  public SearchNfcTagRequestDto(Long assetId, String uid, NfcTagStatus status) {
    this.assetId = assetId;
    this.uid = uid;
    this.status = status;
  }

  public Long getAssetId() { return assetId; }
  public String getUid() { return uid; }
  public NfcTagStatus getStatus() { return status; }
}
