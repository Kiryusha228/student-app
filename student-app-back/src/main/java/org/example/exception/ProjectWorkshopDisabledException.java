package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.LOCKED)
public class ProjectWorkshopDisabledException extends RuntimeException {
  public ProjectWorkshopDisabledException(String message) {
    super(message);
  }
}
