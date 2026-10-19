package com.jabai.campustrack.Services;

import com.jabai.campustrack.DTOs.Responses.PdfParserResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;


@Service
public class PdfParserService {
  private final RestClient restClient;

  public PdfParserService(
          @Value("${spring.application.microservices.sample}") String microServiceUrl
  ) {
    this.restClient = RestClient
            .builder()
            .baseUrl(microServiceUrl)
            .build();
  }

  public List<PdfParserResponseDto> sampleGetAll() {
    return restClient
            .get()
            .uri("/posts")
            .retrieve()
            .body(new ParameterizedTypeReference<List<PdfParserResponseDto>>() { });
  }
}
