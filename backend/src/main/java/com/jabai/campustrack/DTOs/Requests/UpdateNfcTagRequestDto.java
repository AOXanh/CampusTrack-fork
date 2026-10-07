package com.jabai.campustrack.DTOs.Requests;

import com.jabai.campustrack.Models.Enums.NfcTagStatus;
import jakarta.validation.constraints.Size;

public class UpdateNfcTagRequestDto {
  private final Long assetId;

  @Size(max = 100, message = "UID must be 0 to 100 characters only.")
  private final String uid;

  private final NfcTagStatus nfcTagStatus;

  public UpdateNfcTagRequestDto(Long assetId, String uid, NfcTagStatus nfcTagStatus) {
    this.assetId = assetId;
    this.uid = uid;
    this.nfcTagStatus = nfcTagStatus;
  }

  // Getters
  public Long getAssetId() { return assetId; }
  public String getUid() { return uid; }
  public NfcTagStatus getNfcTagStatus() { return nfcTagStatus; }
}
