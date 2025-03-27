package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class StudentAuthenticationException extends RuntimeException {

  public StudentAuthenticationException(String message) {
    super(message);
  }
}
