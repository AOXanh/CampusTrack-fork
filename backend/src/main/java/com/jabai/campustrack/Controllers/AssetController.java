package com.jabai.campustrack.Controllers;

import com.jabai.campustrack.DTOs.Requests.CreateAssetRequestDto;
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
 * <h4>NOTE: DO NOT VIOLATE LAYERS STRUCTURE.</h4>
 * <br/>
 * <p>CHRIS CHAN: Perform search by:</p>
 * <ul>
 *   <li>from (optional) Dapat naka format ha for example 2026-10-07 (CREATED AT)</li>
 *   <li>to (optional) Dapat naka format ha for example 2026-10-07 (CREATED AT)</li>
 *   <li>roomId (optional)</li>
 *   <li>name (optional) -> Dapat naka keyword ni sya</li>
 *   <li>brand (optional) -> Dapat naka keyword ni sya</li>
 *   <li>model (optional) -> Dapat naka keyword ni sya</li>
 *   <li>serialNumber (optional)</li>
 *   <li>category (optional) (DAPAT ENUMS)</li>
 *   <li>condition (optional) (DAPAT ENUMS)</li>
 *   <li>criticality (optional) (DAPAT ENUMS)</li>
 * </ul>
 * <p>P.S.: And dapat naka paginate gihapon sya.</p>
 * <p>Expected URL: <code>/api/assets/search?from=2026-10-07&to=2026-10-08&roomId=1&name=Test+name&brand=Test+brand&model=Test+model&serialNumber=Test+serial&category=COMPUTER&condition=DAMAGED&criticality=CRITICAL</code></p>
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