package com.jabai.campustrack.Exceptions.CustomExceptions;

public class PdfParserException extends RuntimeException {
  private final int status;

  public PdfParserException(String message, int status) {
    super(message);
    this.status = status;
  }

  public int getStatus() {
    return status;
  }
}
