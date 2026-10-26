package com.jabai.campustrack.Services;

import com.jabai.campustrack.DTOs.Requests.PdfParserRequestDto;
import com.jabai.campustrack.DTOs.Responses.PdfParserResponseDto;

import tools.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;


@Service
public class PdfParserService {
  private final RestClient restClient;

  public PdfParserService(
          @Value("${spring.application.microservices.pdf-parser}") String pdfParserUrl
  ) {
    this.restClient = RestClient
            .builder()
            .baseUrl(pdfParserUrl)
            .build();
  }

  public ResponseEntity<PdfParserResponseDto> parsePdf(PdfParserRequestDto pdfParserRequestDto, String Authorization) {
    String json = new ObjectMapper().writeValueAsString(pdfParserRequestDto);
    try {
      PdfParserResponseDto response =
      restClient.post()
      .uri("api/pdf-parser/create/")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .header("Authorization", Authorization)
      .body(json)    
      .retrieve()
      .body(PdfParserResponseDto.class);
      return ResponseEntity.ok(response);
    } catch (RestClientResponseException e) {
      return ResponseEntity.status(e.getStatusCode().value()).body(new PdfParserResponseDto(e.getResponseBodyAsString()));
    }

  }
}
