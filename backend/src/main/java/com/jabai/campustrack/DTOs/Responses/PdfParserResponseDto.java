package com.jabai.campustrack.DTOs.Responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PdfParserResponseDto {
  private final String filename;
  private final String link;
  private final String message;

  @JsonCreator
  public PdfParserResponseDto(
    @JsonProperty("filename") String filename, 
    @JsonProperty("link") String link) {
    this.filename = filename;
    this.link = link;
    this.message = null;
  }

  public PdfParserResponseDto(String message) {
    this.message = message;
    this.filename = null;
    this.link = null;
  }

  // Getters
 public String getFilename() {
     return filename;
 }
 public String getLink() {
     return link;
 }
 public String getMessage() {
     return message;
 }
}
