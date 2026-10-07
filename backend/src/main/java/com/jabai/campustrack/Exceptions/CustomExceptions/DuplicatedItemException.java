package com.jabai.campustrack.Exceptions.CustomExceptions;

public class DuplicatedItemException extends RuntimeException {
  public DuplicatedItemException(String message) {
    super(message);
  }
}
