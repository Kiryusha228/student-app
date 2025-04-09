package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.LOCKED)
public class ProjectWorkshopEnabledException extends RuntimeException {
  public ProjectWorkshopEnabledException(String message) {
    super(message);
  }
}
