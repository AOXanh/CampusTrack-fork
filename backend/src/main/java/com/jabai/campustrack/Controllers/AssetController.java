package com.jabai.campustrack.Controllers;

import com.jabai.campustrack.DTOs.Requests.CreateAssetRequestDto;
import com.jabai.campustrack.DTOs.Requests.SearchAssetRequestDto;
import com.jabai.campustrack.DTOs.Requests.UpdateAssetRequestDto;
import com.jabai.campustrack.DTOs.Responses.AssetResponseDto;
import com.jabai.campustrack.Services.AssetService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <h1>AssetController: CHRIS CHAN</h1>
 */
@RestController
@RequestMapping("/api/assets")
public class AssetController {
  private final AssetService assetService;

  public AssetController(AssetService assetService) {
    this.assetService = assetService;
  }

  // Create
  @PostMapping
  public ResponseEntity<AssetResponseDto> createAsset(@Valid @RequestBody CreateAssetRequestDto request) {
    AssetResponseDto createdAsset = assetService.createAsset(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdAsset);
  }

  // Read all
  @GetMapping
  public ResponseEntity<Page<AssetResponseDto>> getAllAssets(@PageableDefault(size = 20) Pageable pageable) {
    Page<AssetResponseDto> assets = assetService.getAllAssets(pageable);
    return ResponseEntity.ok(assets);
  }

  // Search
  // this must stay above "/{id}" so "search" is not read as an id
  @GetMapping("/search")
  public ResponseEntity<Page<AssetResponseDto>> searchAssets(@Valid SearchAssetRequestDto request, @PageableDefault(size = 20) Pageable pageable) {
    Page<AssetResponseDto> assets = assetService.searchAssets(request, pageable);
    return ResponseEntity.ok(assets);
  }

  // Read
  @GetMapping("/{id}")
  public ResponseEntity<AssetResponseDto> getAssetById(@PathVariable Long id) {
    AssetResponseDto asset = assetService.getAssetById(id);
    return ResponseEntity.ok(asset);
  }

  // Update
  @PatchMapping("/{id}")
  public ResponseEntity<AssetResponseDto> updateAsset(@PathVariable Long id, @Valid @RequestBody UpdateAssetRequestDto request) {
    AssetResponseDto updatedAsset = assetService.updateAsset(id, request);
    return ResponseEntity.ok(updatedAsset);
  }

  // Delete
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
    assetService.deleteAsset(id);
    return ResponseEntity.noContent().build();
  }
}