package com.jabai.campustrack.DTOs.Responses;

import com.jabai.campustrack.Models.Enums.NfcTagStatus;

import java.time.LocalDateTime;

public class NfcTagResponseDto {
  private final long id;
  private final LocalDateTime createdAt;
  private final AssetResponseDto asset;
  private final String uid;
  private final NfcTagStatus status;

  public NfcTagResponseDto(
          long id,
          LocalDateTime createdAt,
          AssetResponseDto asset,
          String uid,
          NfcTagStatus status
  ) {
    this.id = id;
    this.createdAt = createdAt;
    this.asset = asset;
    this.uid = uid;
    this.status = status;
  }

  // Getters
  public long getId() { return id; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public AssetResponseDto getAsset() { return asset; }
  public String getUid() { return uid; }
  public NfcTagStatus getStatus() { return status; }
}
