package org.example.service.impl;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.exception.ProjectWorkshopNotFoundException;
import org.example.mapper.ProjectWorkshopMapper;
import org.example.mapper.StudentProjectWorkshopMapper;
import org.example.model.dto.database.CreateProjectWorkshopDto;
import org.example.model.dto.database.ProjectWorkshopDto;
import org.example.model.dto.database.StudentInTeamDto;
import org.example.model.dto.database.TeamWithStudentInfoDto;
import org.example.model.entity.StudentProjectWorkshopEntity;
import org.example.repository.ProjectWorkshopRepository;
import org.example.service.ProjectWorkshopService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectWorkshopServiceImpl implements ProjectWorkshopService {
  private final StudentProjectWorkshopMapper studentProjectWorkshopMapper;
  private final ProjectWorkshopRepository projectWorkshopRepository;
  private final ProjectWorkshopMapper projectWorkshopMapper;

  @Override
  public void createProjectWorkshop(CreateProjectWorkshopDto createProjectWorkshopDto) {
    projectWorkshopRepository.save(
        projectWorkshopMapper.toProjectWorkshopEntity(createProjectWorkshopDto));
  }

  @Override
  @Transactional
  public void addStudent(
      Long projectWorkshopId, StudentProjectWorkshopEntity studentProjectWorkshop) {
    var projectWorkshopOptional = projectWorkshopRepository.findById(projectWorkshopId);

    if (projectWorkshopOptional.isEmpty()) {
      throw new ProjectWorkshopNotFoundException("Такой мастерской не существует");
    }
    var projectWorkshop = projectWorkshopOptional.get();

    projectWorkshop.getStudentProjectWorkshop().add(studentProjectWorkshop);
    studentProjectWorkshop.setProjectWorkshop(projectWorkshop);
  }

  @Override
  public List<ProjectWorkshopDto> getProjectWorkshops() {
    var projectWorkshops = projectWorkshopRepository.findAll();
    var projectWorkshopsDto = new ArrayList<ProjectWorkshopDto>();

    for (var projectWorkshop : projectWorkshops) {
      projectWorkshopsDto.add(projectWorkshopMapper.toProjectWorkshopDto(projectWorkshop));
    }

    return projectWorkshopsDto;
  }

  @Override
  public ProjectWorkshopDto getProjectWorkshopById(Long projectWorkshopId) {
    var projectWorkshop = projectWorkshopRepository.findById(projectWorkshopId);

    if (projectWorkshop.isEmpty()) {
      throw new ProjectWorkshopNotFoundException(
          "Мастерская с ID " + projectWorkshopId + " не найдена");
    }

    return projectWorkshopMapper.toProjectWorkshopDto(projectWorkshop.get());
  }

  @Override
  public Long getLastProjectWorkshopId() {
    // todo: возможно сделать лучше
    var projectWorkshops = projectWorkshopRepository.findAll();
    return projectWorkshops.get(projectWorkshops.size() - 1).getId();
  }

  @Override
  public List<TeamWithStudentInfoDto> getTeams(Long projectWorkshopId) {
    var projectWorkshop = projectWorkshopRepository.findById(projectWorkshopId);
    if (projectWorkshop.isEmpty()) {
      throw new ProjectWorkshopNotFoundException("Мастерская не найдена");
    }
    var teams = projectWorkshop.get().getTeams();
    var teamsWithInfo = new ArrayList<TeamWithStudentInfoDto>();
    for (var team : teams) {
      var students = team.getStudentProjectWorkshop();
      var studentsInTeamWithInfo = new ArrayList<StudentInTeamDto>();
      for (var student : students) {
        studentsInTeamWithInfo.add(studentProjectWorkshopMapper.toStudentInTeamDto(student));
      }
      teamsWithInfo.add(new TeamWithStudentInfoDto(team.getId(), studentsInTeamWithInfo));
    }
    return teamsWithInfo;
  }
}
