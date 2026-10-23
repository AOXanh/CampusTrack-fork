package com.jabai.campustrack.DTOs.Responses;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PdfParserResponseDto {
  /**
   * SAMPLE RANI SYA
   */
  private final Integer userId;
  private final Integer id;
  private final String title;
  private final String body;

  public PdfParserResponseDto(Integer userId, Integer id, String title, String body) {
    this.userId = userId;
    this.id = id;
    this.title = title;
    this.body = body;
  }

  // Getters
  public String getBody() { return body; }
  public String getTitle() { return title; }
  public Integer getId() { return id; }
  public Integer getUserId() { return userId; }
}
