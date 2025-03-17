package org.example.model.dto.exception;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionDto {
  public String message;
  public Map<String, Object> info;
}
