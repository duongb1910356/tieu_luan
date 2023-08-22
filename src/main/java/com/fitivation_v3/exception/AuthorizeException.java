package com.fitivation_v3.exception;

public class AuthorizeException extends RuntimeException{
  public AuthorizeException() {
    this("Authorization failed");
  }

  public AuthorizeException(String message) {
    super(message);
  }
}
