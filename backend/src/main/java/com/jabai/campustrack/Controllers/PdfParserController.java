package com.jabai.campustrack.Controllers;

import com.jabai.campustrack.DTOs.Requests.PdfParserRequestDto;
import com.jabai.campustrack.DTOs.Responses.PdfParserResponseDto;
import com.jabai.campustrack.Services.PdfParserService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h1>PdfParserController: Kyle</h1>
 */
@RestController
@RequestMapping("/api/pdf-parser")
public class PdfParserController {
  private final PdfParserService pdfParserService;

  public PdfParserController(PdfParserService pdfParserService) {
    this.pdfParserService = pdfParserService;
  }

  @PostMapping
  public ResponseEntity<PdfParserResponseDto> createIncidentReport(
          @Valid @RequestBody PdfParserRequestDto createIncidentReportRequestDto,
          @RequestHeader("Authorization") String Authorization) {
    return pdfParserService.parsePdf(createIncidentReportRequestDto, Authorization);
  }
}
