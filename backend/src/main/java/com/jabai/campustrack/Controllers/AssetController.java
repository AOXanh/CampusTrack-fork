package com.jabai.campustrack.Controllers;

import com.jabai.campustrack.DTOs.Requests.CreateAssetRequestDto;
import com.jabai.campustrack.DTOs.Requests.UpdateAssetRequestDto;
import com.jabai.campustrack.DTOs.Responses.AssetResponseDto;
import com.jabai.campustrack.Services.AssetService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <h4>NOTE: DO NOT VIOLATE LAYERS STRUCTURE.</h4>
 * <br/>
 * <p>CHRIS CHAN: Pag add ug paginatiom sa imohang read all gamit ang spring data extension</p>
 */
@RestController
@RequestMapping("/api/assets")
public class AssetController {
  private final AssetService assetService;

  public AssetController(AssetService assetService) {
    this.assetService = assetService;
  }

  // create
  @PostMapping
  public ResponseEntity<AssetResponseDto> createAsset(@Valid @RequestBody CreateAssetRequestDto request) {
    AssetResponseDto createdAsset = assetService.createAsset(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdAsset);
  }

  // read all
  @GetMapping
  public ResponseEntity<Page<AssetResponseDto>> getAllAssets(Pageable pageable) {
    Page<AssetResponseDto> assets = assetService.getAllAssets(pageable);
    return ResponseEntity.ok(assets);
  }

  // read
  @GetMapping("/{id}")
  public ResponseEntity<AssetResponseDto> getAssetById(@PathVariable Long id) {
    AssetResponseDto asset = assetService.getAssetById(id);
    return ResponseEntity.ok(asset);
  }

  // update
  @PutMapping("/{id}")
  public ResponseEntity<AssetResponseDto> updateAsset(@PathVariable Long id, @Valid @RequestBody UpdateAssetRequestDto request) {
    AssetResponseDto updatedAsset = assetService.updateAsset(id, request);
    return ResponseEntity.ok(updatedAsset);
  }

  // delete
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
    assetService.deleteAsset(id);
    return ResponseEntity.noContent().build();
  }
}