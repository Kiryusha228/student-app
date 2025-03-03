package org.example.model.dto;

import lombok.*;

@AllArgsConstructor
@Data
public class TestDto {
    private Long id;
    private Integer testResult;
    private Integer contestResult;
    private Long studentId;
}
