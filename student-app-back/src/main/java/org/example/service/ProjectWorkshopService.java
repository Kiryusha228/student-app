package org.example.service;

import java.util.List;
import org.example.model.dto.database.CreateProjectWorkshopDto;
import org.example.model.dto.database.ProjectWorkshopDto;
import org.example.model.dto.database.TeamWithStudentInfoDto;
import org.example.model.entity.StudentProjectWorkshopEntity;

public interface ProjectWorkshopService {
  void createProjectWorkshop(CreateProjectWorkshopDto createProjectWorkshopDto);

  void addStudent(Long projectWorkshopId, StudentProjectWorkshopEntity studentProjectWorkshop);

  List<ProjectWorkshopDto> getProjectWorkshops();

  ProjectWorkshopDto getProjectWorkshopById(Long projectWorkshopId);

  ProjectWorkshopDto getLastProjectWorkshop();

  Long getLastProjectWorkshopId();

  List<TeamWithStudentInfoDto> getTeams(Long projectWorkshopId);

  void enableProjectWorkshop(Long projectWorkshopId);

  void disableProjectWorkshop(Long projectWorkshopId);
}
