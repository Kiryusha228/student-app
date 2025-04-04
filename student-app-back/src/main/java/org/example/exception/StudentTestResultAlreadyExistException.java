package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class StudentTestResultAlreadyExistException extends RuntimeException {
  public StudentTestResultAlreadyExistException(String message) {
    super(message);
  }
}
