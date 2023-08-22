package com.fitivation_v3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class APIExceptionHandler {
  @ExceptionHandler
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleAllException(Exception ex, WebRequest req) {
    return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, WebRequest req) {
    ErrorResponse res = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    return new ResponseEntity<>(res, res.getStatus());
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleNotFoundException(BadRequestException ex, WebRequest req) {
    ErrorResponse res = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    return new ResponseEntity<>(res, res.getStatus());
  }

  @ExceptionHandler(AuthorizeException.class)
  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  public ResponseEntity<ErrorResponse> handleUnauthorizedException(AuthorizeException ex, WebRequest req) {
    ErrorResponse res = new ErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    return new ResponseEntity<>(res, res.getStatus());
  }
}
