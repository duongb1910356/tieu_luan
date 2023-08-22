package com.fitivation_v3.exception;

public class NotFoundException extends RuntimeException {
  public AuthorizeException() {
    this("Requested resource not found");
  }

  public NotFoundException(String message) {
    super(message);
  }
}
