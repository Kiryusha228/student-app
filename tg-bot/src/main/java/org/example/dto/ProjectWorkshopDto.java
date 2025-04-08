package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectWorkshopDto {
  private Long id;
  private String name;
  private Integer year;
  private LocalDateTime startDateTime;
  private LocalDateTime endDateTime;
  private Boolean isEnable;
}
