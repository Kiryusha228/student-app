package org.example.mapper;

import org.example.model.dto.database.CreateProjectWorkshopDto;
import org.example.model.entity.ProjectWorkshopEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectWorkshopMapper {
  public ProjectWorkshopEntity toProjectWorkshopEntity(
      CreateProjectWorkshopDto createProjectWorkshopDto) {
    return new ProjectWorkshopEntity(
        0L, createProjectWorkshopDto.getName(), createProjectWorkshopDto.getYear(), null);
  }
}
