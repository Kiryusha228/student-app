package ru.tbank.model.dto.database;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
