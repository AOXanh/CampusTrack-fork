package com.jabai.campustrack.Controllers;

import com.jabai.campustrack.DTOs.Responses.PdfParserResponseDto;
import com.jabai.campustrack.Services.PdfParserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <h1>PdfParserController: Kyle</h1>
 *
 * <p>E connect ang PDF parser sa micro-service dri bai</p>
 * <p>Gamita ang PdfParserService ngadto sa services</p>
 * <p>Requirements:</p>
 * <ul>
 *   <li>Dapat naay Origin gikan sa backend</li>
 *   <li>Ang Origin kay provided ngadto sa application properties</li>
 *   <li>Refer ngadto sa <code>pdf_parser/schemas.py</code> para sa request and response DTO niya</li>
 *   <li>STRICTLY only gamit ug RestClient para sa requests niya to microservices</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/pdf-parser")
public class PdfParserController {
  private final PdfParserService pdfParserService;

  public PdfParserController(PdfParserService pdfParserService) {
    this.pdfParserService = pdfParserService;
  }

  @GetMapping("/sample")
  public ResponseEntity<List<PdfParserResponseDto>> sample() {
    List<PdfParserResponseDto> response = pdfParserService.sampleGetAll();
    return ResponseEntity.ok(response);
  }
}
