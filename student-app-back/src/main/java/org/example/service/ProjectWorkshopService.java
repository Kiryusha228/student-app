package org.example.service;

import org.example.model.dto.database.CreateProjectWorkshopDto;
import org.example.model.entity.StudentProjectWorkshopEntity;

public interface ProjectWorkshopService {
  void createProjectWorkshop(CreateProjectWorkshopDto createProjectWorkshopDto);

  void addStudent(Long projectWorkshopId, StudentProjectWorkshopEntity studentProjectWorkshop);

  Long getLastProjectWorkshopId();
}
