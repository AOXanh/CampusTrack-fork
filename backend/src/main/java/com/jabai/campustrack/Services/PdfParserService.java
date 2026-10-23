package com.jabai.campustrack.Services;

import com.jabai.campustrack.DTOs.Requests.PdfParserRequestDto;
import com.jabai.campustrack.DTOs.Responses.PdfParserResponseDto;
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
          @Value("${spring.application.microservices.sample}") String microServiceUrl,
          @Value("${spring.application.microservices.pdf-parser}") String pdfParserUrl
  ) {
    this.restClient = RestClient
            .builder()
            .baseUrl(pdfParserUrl)
            .build();
  }

  public ResponseEntity<PdfParserResponseDto> parsePdf(PdfParserRequestDto pdfParserRequestDto, String Authorization) {
    try {
      return ResponseEntity.ok(
      restClient.post()
      .uri("create/")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .header("Authorization", Authorization)
      .body(pdfParserRequestDto)    
      .retrieve()
      .body(PdfParserResponseDto.class)
      );
    } catch (RestClientResponseException e) {
      return ResponseEntity.status(e.getStatusCode().value()).body(new PdfParserResponseDto(null, null, null, e.getResponseBodyAsString()));
    }
  }


  public List<PdfParserResponseDto> sampleGetAll() {
    return restClient
            .get()
            .uri("/posts")
            .retrieve()
            .body(new ParameterizedTypeReference<>() { });
  }
}
