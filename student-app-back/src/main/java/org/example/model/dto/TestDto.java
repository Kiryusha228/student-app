package org.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TestDto {
    private Long id;
    private Integer testResult;
    private Integer contestResult;
    private Long studentId;
}
