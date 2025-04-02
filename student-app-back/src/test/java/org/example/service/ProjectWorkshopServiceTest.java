package org.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.exception.ProjectWorkshopNotFoundException;
import org.example.mapper.ProjectWorkshopMapper;
import org.example.mapper.StudentProjectWorkshopMapper;
import org.example.model.dto.database.CreateProjectWorkshopDto;
import org.example.model.dto.database.ProjectWorkshopDto;
import org.example.model.dto.database.StudentInTeamDto;
import org.example.model.dto.database.TeamWithStudentInfoDto;
import org.example.model.entity.ProjectWorkshopEntity;
import org.example.model.entity.StudentEntity;
import org.example.model.entity.StudentProjectWorkshopEntity;
import org.example.model.entity.TeamEntity;
import org.example.repository.ProjectWorkshopRepository;
import org.example.service.impl.ProjectWorkshopServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectWorkshopServiceTest {
  @Mock private ProjectWorkshopRepository projectWorkshopRepository;

  @Mock private ProjectWorkshopMapper projectWorkshopMapper;

  @Mock private StudentProjectWorkshopMapper studentProjectWorkshopMapper;

  @InjectMocks private ProjectWorkshopServiceImpl projectWorkshopService;

  @Test
  void createProjectWorkshop() {
    // Arrange
    var createProjectWorkshopDto = new CreateProjectWorkshopDto();
    createProjectWorkshopDto.setName("Test Workshop");
    createProjectWorkshopDto.setYear(2025);

    var projectWorkshopEntity = new ProjectWorkshopEntity();
    projectWorkshopEntity.setName("Test Workshop");
    projectWorkshopEntity.setYear(2025);

    when(projectWorkshopMapper.toProjectWorkshopEntity(createProjectWorkshopDto))
        .thenReturn(projectWorkshopEntity);

    // Act
    projectWorkshopService.createProjectWorkshop(createProjectWorkshopDto);

    // Assert
    // используем verify тк метод void
    verify(projectWorkshopMapper).toProjectWorkshopEntity(createProjectWorkshopDto);
    verify(projectWorkshopRepository).save(projectWorkshopEntity);
  }

  @Test
  void addStudent() {
    // Arrange
    var projectWorkshopId = 1L;

    var projectWorkshop = new ProjectWorkshopEntity();
    projectWorkshop.setId(projectWorkshopId);
    projectWorkshop.setName("Test Workshop");
    projectWorkshop.setYear(2025);
    projectWorkshop.setIsEnable(true);
    projectWorkshop.setStudentProjectWorkshop(new ArrayList<>());

    var student = new StudentEntity();
    student.setName("student");
    student.setMail("student@mail");

    var studentProjectWorkshop = new StudentProjectWorkshopEntity();
    studentProjectWorkshop.setId(1L);
    studentProjectWorkshop.setStudent(student);
    studentProjectWorkshop.setProjectWorkshop(projectWorkshop);

    when(projectWorkshopRepository.findById(projectWorkshopId))
        .thenReturn(Optional.of(projectWorkshop));

    // Act
    projectWorkshopService.addStudent(projectWorkshopId, studentProjectWorkshop);

    // Assert
    // verify(projectWorkshopRepository).save(projectWorkshop); не нужно тк явно save не вызывается

    assertTrue(projectWorkshop.getStudentProjectWorkshop().contains(studentProjectWorkshop));
    assertEquals(projectWorkshop, studentProjectWorkshop.getProjectWorkshop());
  }

  @Test
  void addStudentWithProjectWorkshopNotFoundException() {
    // Assert
    var projectWorkshopId = 1L;

    var studentWorkshopEntity = new StudentProjectWorkshopEntity();

    when(projectWorkshopRepository.findById(projectWorkshopId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        ProjectWorkshopNotFoundException.class,
        () -> {
          projectWorkshopService.addStudent(projectWorkshopId, studentWorkshopEntity);
        });
  }

  @Test
  void getProjectWorkshops() {
    // Arrange
    var projectWorkshopEntity1 = new ProjectWorkshopEntity();
    projectWorkshopEntity1.setId(1L);
    projectWorkshopEntity1.setName("Test Workshop 1");
    projectWorkshopEntity1.setYear(2025);

    var projectWorkshopEntity2 = new ProjectWorkshopEntity();
    projectWorkshopEntity2.setId(2L);
    projectWorkshopEntity2.setName("Test Workshop 2");
    projectWorkshopEntity2.setYear(2025);

    var projectWorkshopEntityList = List.of(projectWorkshopEntity1, projectWorkshopEntity2);

    var projectWorkshopDto1 = new ProjectWorkshopDto();
    projectWorkshopDto1.setId(1L);
    projectWorkshopDto1.setName("Test Workshop 1");
    projectWorkshopDto1.setYear(2025);

    var projectWorkshopDto2 = new ProjectWorkshopDto();
    projectWorkshopDto2.setId(2L);
    projectWorkshopDto2.setName("Test Workshop 2");
    projectWorkshopDto2.setYear(2025);

    var expectedProjectWorkshopDtoList = List.of(projectWorkshopDto1, projectWorkshopDto2);

    when(projectWorkshopRepository.findAll()).thenReturn(projectWorkshopEntityList);
    when(projectWorkshopMapper.toProjectWorkshopDto(projectWorkshopEntity1))
        .thenReturn(projectWorkshopDto1);
    when(projectWorkshopMapper.toProjectWorkshopDto(projectWorkshopEntity2))
        .thenReturn(projectWorkshopDto2);

    // Act
    var gettedProjectWorkshopDtoList = projectWorkshopService.getProjectWorkshops();

    // Assert
    assertEquals(expectedProjectWorkshopDtoList, gettedProjectWorkshopDtoList);
  }

  @Test
  void getProjectWorkshopById() {
    // Arrange
    var projectWorkshopId = 1L;

    var projectWorkshopEntity = new ProjectWorkshopEntity();
    projectWorkshopEntity.setId(1L);
    projectWorkshopEntity.setName("Test Workshop");
    projectWorkshopEntity.setYear(2025);

    var projectWorkshopDto = new ProjectWorkshopDto();
    projectWorkshopDto.setId(1L);
    projectWorkshopDto.setName("Test Workshop");
    projectWorkshopDto.setYear(2025);

    when(projectWorkshopRepository.findById(projectWorkshopId))
        .thenReturn(Optional.of(projectWorkshopEntity));
    when(projectWorkshopMapper.toProjectWorkshopDto(projectWorkshopEntity))
        .thenReturn(projectWorkshopDto);

    // Act
    var gettedProjectWorkshopDto = projectWorkshopService.getProjectWorkshopById(projectWorkshopId);

    // Assert
    assertEquals(projectWorkshopDto, gettedProjectWorkshopDto);
  }

  @Test
  void getProjectWorkshopByIdWithProjectWorkshopNotFoundException() {
    // Arrange
    var projectWorkshopId = 1L;

    when(projectWorkshopRepository.findById(projectWorkshopId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        ProjectWorkshopNotFoundException.class,
        () -> {
          projectWorkshopService.getProjectWorkshopById(projectWorkshopId);
        });
  }

  @Test
  void getLastProjectWorkshopId() {
    // Arrange
    var projectWorkshopEntity1 = new ProjectWorkshopEntity();
    projectWorkshopEntity1.setId(1L);
    projectWorkshopEntity1.setName("Test Workshop 1");
    projectWorkshopEntity1.setYear(2025);

    var projectWorkshopEntity2 = new ProjectWorkshopEntity();
    projectWorkshopEntity2.setId(2L);
    projectWorkshopEntity2.setName("Test Workshop 2");
    projectWorkshopEntity2.setYear(2025);

    var projectWorkshopEntityList = List.of(projectWorkshopEntity1, projectWorkshopEntity2);

    var expectedProjectWorkshopId = 2L;

    when(projectWorkshopRepository.findAll()).thenReturn(projectWorkshopEntityList);

    // Act
    var gettedId = projectWorkshopService.getLastProjectWorkshopId();

    // Assert
    assertEquals(expectedProjectWorkshopId, gettedId);
  }

  @Test
  void getTeams() {
    // Arrange
    var projectWorkshopId = 1L;

    var projectWorkshopEntity = new ProjectWorkshopEntity();
    projectWorkshopEntity.setId(1L);
    projectWorkshopEntity.setName("Test Workshop");
    projectWorkshopEntity.setYear(2025);

    var student1 = new StudentEntity();
    student1.setName("student1");
    student1.setMail("student1@mail");

    var student2 = new StudentEntity();
    student2.setName("student2");
    student2.setMail("student2@mail");

    var studentProjectWorkshop1 = new StudentProjectWorkshopEntity();
    studentProjectWorkshop1.setId(1L);
    studentProjectWorkshop1.setStudent(student1);
    studentProjectWorkshop1.setProjectWorkshop(projectWorkshopEntity);

    var studentProjectWorkshop2 = new StudentProjectWorkshopEntity();
    studentProjectWorkshop2.setId(1L);
    studentProjectWorkshop2.setStudent(student2);
    studentProjectWorkshop2.setProjectWorkshop(projectWorkshopEntity);

    var team1 = new TeamEntity();
    team1.setProjectWorkshop(projectWorkshopEntity);
    team1.setStudentProjectWorkshop(List.of(studentProjectWorkshop1));
    team1.setId(1L);

    var team2 = new TeamEntity();
    team2.setProjectWorkshop(projectWorkshopEntity);
    team2.setStudentProjectWorkshop(List.of(studentProjectWorkshop2));
    team2.setId(1L);

    projectWorkshopEntity.setTeams(List.of(team1, team2));

    var student1InTeamDto = new StudentInTeamDto();
    student1InTeamDto.setName(student1.getName());
    student1InTeamDto.setRole("role1");
    student1InTeamDto.setTelegram("telegram1");

    var student2InTeamDto = new StudentInTeamDto();
    student2InTeamDto.setName(student2.getName());
    student2InTeamDto.setRole("role2");
    student2InTeamDto.setTelegram("telegram2");

    var teamWithStudent1InfoDto = new TeamWithStudentInfoDto();
    teamWithStudent1InfoDto.setId(team1.getId());
    teamWithStudent1InfoDto.setStudents(List.of(student1InTeamDto));

    var teamWithStudent2InfoDto = new TeamWithStudentInfoDto();
    teamWithStudent2InfoDto.setId(team2.getId());
    teamWithStudent2InfoDto.setStudents(List.of(student2InTeamDto));

    var expectedTeamsWithInfo = new ArrayList<TeamWithStudentInfoDto>();
    expectedTeamsWithInfo.add(teamWithStudent1InfoDto);
    expectedTeamsWithInfo.add(teamWithStudent2InfoDto);

    when(studentProjectWorkshopMapper.toStudentInTeamDto(studentProjectWorkshop1))
        .thenReturn(student1InTeamDto);
    when(studentProjectWorkshopMapper.toStudentInTeamDto(studentProjectWorkshop2))
        .thenReturn(student2InTeamDto);
    when(projectWorkshopRepository.findById(projectWorkshopId))
        .thenReturn(Optional.of(projectWorkshopEntity));

    // Act
    var gettedTeamsWithInfo = projectWorkshopService.getTeams(projectWorkshopId);

    // Assert
    assertEquals(expectedTeamsWithInfo, gettedTeamsWithInfo);
  }

  @Test
  void getTeamsWithProjectWorkshopNotFoundException() {
    // Arrange
    var projectWorkshopId = 1L;

    when(projectWorkshopRepository.findById(projectWorkshopId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        ProjectWorkshopNotFoundException.class,
        () -> {
          projectWorkshopService.getTeams(projectWorkshopId);
        });
  }
}
