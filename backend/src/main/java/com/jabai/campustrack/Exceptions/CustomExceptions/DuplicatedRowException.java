package com.jabai.campustrack.Exceptions.CustomExceptions;

public class DuplicatedRowException extends RuntimeException {
  public DuplicatedRowException(String message) {
    super(message);
  }
}
