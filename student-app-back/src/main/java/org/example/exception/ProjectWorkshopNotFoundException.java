package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProjectWorkshopNotFoundException extends RuntimeException {
  public ProjectWorkshopNotFoundException(String message) {
    super(message);
  }
}
