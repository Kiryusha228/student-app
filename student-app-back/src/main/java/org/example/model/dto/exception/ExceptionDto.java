package org.example.model.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ExceptionDto {
    public String message;
    public Map<String, Object> info;
}
