package org.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.example.exception.ProjectWorkshopNotFoundException;
import org.example.exception.StudentNotFoundException;
import org.example.exception.StudentProjectWorkshopNotFoundException;
import org.example.mapper.StudentProjectWorkshopMapper;
import org.example.model.dto.database.StudentInTeamDto;
import org.example.model.dto.database.StudentInfoDto;
import org.example.model.entity.*;
import org.example.repository.ProjectWorkshopRepository;
import org.example.repository.StudentProjectWorkshopRepository;
import org.example.repository.StudentRepository;
import org.example.service.impl.StudentProjectWorkshopServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentProjectWorkshopServiceTest {
  @Mock private StudentRepository studentRepository;
  @Mock private ProjectWorkshopRepository projectWorkshopRepository;
  @Mock private StudentProjectWorkshopRepository studentProjectWorkshopRepository;
  @Mock private ProjectWorkshopService projectWorkshopService;
  @Mock private StudentProjectWorkshopMapper studentProjectWorkshopMapper;

  @InjectMocks private StudentProjectWorkshopServiceImpl studentProjectWorkshopService;

  @Test
  void getAllPastStudents() {
    // Arrange
    var lastProjectWorkshopId = 1L;
    when(projectWorkshopService.getLastProjectWorkshopId()).thenReturn(lastProjectWorkshopId);

    var projectWorkshop = new ProjectWorkshopEntity();
    projectWorkshop.setId(lastProjectWorkshopId);

    var student1Questionnaire = new QuestionnaireEntity();
    student1Questionnaire.setId(1L);

    var student2Questionnaire = new QuestionnaireEntity();
    student2Questionnaire.setId(2L);

    var student1TestResult = new StudentTestResultEntity();
    student1TestResult.setTestResult(1);

    var student2TestResult = new StudentTestResultEntity();
    student2TestResult.setTestResult(2);

    var student1ProjectWorkshop = new StudentProjectWorkshopEntity();
    student1ProjectWorkshop.setId(1L);
    student1ProjectWorkshop.setQuestionnaire(student1Questionnaire);
    student1ProjectWorkshop.setStudentTestResult(student1TestResult);
    student1ProjectWorkshop.setProjectWorkshop(projectWorkshop);

    var student2ProjectWorkshop = new StudentProjectWorkshopEntity();
    student2ProjectWorkshop.setId(2L);
    student2ProjectWorkshop.setQuestionnaire(student2Questionnaire);
    student2ProjectWorkshop.setStudentTestResult(student2TestResult);
    student2ProjectWorkshop.setProjectWorkshop(projectWorkshop);

    var studentsList = List.of(student1ProjectWorkshop, student2ProjectWorkshop);

    when(studentProjectWorkshopRepository.findAll()).thenReturn(studentsList);

    var student1InfoDto = new StudentInfoDto();
    student1InfoDto.setStudentProjectWorkshopId(student1ProjectWorkshop.getId());

    var student2InfoDto = new StudentInfoDto();
    student2InfoDto.setStudentProjectWorkshopId(student2ProjectWorkshop.getId());

    when(studentProjectWorkshopMapper.toStudentInfoDto(student1ProjectWorkshop))
        .thenReturn(student1InfoDto);
    when(studentProjectWorkshopMapper.toStudentInfoDto(student2ProjectWorkshop))
        .thenReturn(student2InfoDto);

    var expectedStudentInfoList = List.of(student1InfoDto, student2InfoDto);

    // Act
    var gettedStudentInfoList = studentProjectWorkshopService.getAllPastStudents();

    // Assert
    assertEquals(expectedStudentInfoList, gettedStudentInfoList);
  }

  @Test
  void getStudentProjectWorkshopById() {
    // Arrange
    var studentProjectWorkshopId = 1L;
    var student = new StudentProjectWorkshopEntity();
    student.setId(studentProjectWorkshopId);
    when(studentProjectWorkshopRepository.findById(studentProjectWorkshopId))
        .thenReturn(Optional.of(student));
    // Act
    var gettedStudent =
        studentProjectWorkshopService.getStudentProjectWorkshopById(studentProjectWorkshopId);

    // Assert
    assertEquals(student, gettedStudent);
  }

  @Test
  void getStudentProjectWorkshopByIdWithStudentProjectWorkshopNotFoundException() {
    // Arrange
    var studentProjectWorkshopId = 1L;
    when(studentProjectWorkshopRepository.findById(studentProjectWorkshopId))
        .thenReturn(Optional.empty());
    // Act & Assert
    assertThrows(
        StudentProjectWorkshopNotFoundException.class,
        () -> {
          studentProjectWorkshopService.getStudentProjectWorkshopById(studentProjectWorkshopId);
        });
  }

  @Test
  void createStudentProjectWorkshop() {
    // Arrange
    var studentId = "studentId";
    var student = new StudentEntity();
    student.setId(studentId);

    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

    var lastProjectWorkshopId = 1L;
    var projectWorkshop = new ProjectWorkshopEntity();
    projectWorkshop.setId(lastProjectWorkshopId);

    when(projectWorkshopService.getLastProjectWorkshopId()).thenReturn(lastProjectWorkshopId);
    when(projectWorkshopRepository.findById(lastProjectWorkshopId))
        .thenReturn(Optional.of(projectWorkshop));

    var studentProjectWorkshop = new StudentProjectWorkshopEntity();
    studentProjectWorkshop.setId(0L);
    studentProjectWorkshop.setStudent(student);
    studentProjectWorkshop.setProjectWorkshop(projectWorkshop);

    var savedStudentProjectWorkshop = new StudentProjectWorkshopEntity();
    savedStudentProjectWorkshop.setId(1L);
    savedStudentProjectWorkshop.setStudent(student);
    savedStudentProjectWorkshop.setProjectWorkshop(projectWorkshop);

    when(studentProjectWorkshopRepository.save(studentProjectWorkshop))
        .thenReturn(savedStudentProjectWorkshop);

    // Act
    studentProjectWorkshopService.createStudentProjectWorkshop(studentId);

    // Assert
    verify(studentProjectWorkshopRepository).save(studentProjectWorkshop);
    verify(projectWorkshopService).addStudent(lastProjectWorkshopId, savedStudentProjectWorkshop);
  }

  @Test
  void createStudentProjectWorkshopWithStudentNotFoundException() {
    // Arrange
    var studentId = "studentId";
    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        StudentNotFoundException.class,
        () -> {
          studentProjectWorkshopService.createStudentProjectWorkshop(studentId);
        });
  }

  @Test
  void createStudentProjectWorkshopWithProjectWorkshopNotFoundException() {
    // Arrange
    var studentId = "studentId";
    var student = new StudentEntity();
    student.setId(studentId);

    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

    var projectWorkshopId = 1L;
    when(projectWorkshopService.getLastProjectWorkshopId()).thenReturn(projectWorkshopId);

    when(projectWorkshopRepository.findById(projectWorkshopId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        ProjectWorkshopNotFoundException.class,
        () -> {
          studentProjectWorkshopService.createStudentProjectWorkshop(studentId);
        });
  }

  @Test
  void getTeam() {
    // Arrange
    var studentId = "studentId";
    var student = new StudentEntity();
    student.setId(studentId);

    var student1ProjectWorkshop = new StudentProjectWorkshopEntity();
    student1ProjectWorkshop.setId(1L);

    var student2ProjectWorkshop = new StudentProjectWorkshopEntity();
    student2ProjectWorkshop.setId(2L);

    student.setStudentProjectWorkshop(List.of(student1ProjectWorkshop));

    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

    var team = new TeamEntity();
    team.setId(1L);
    team.setStudentProjectWorkshop(List.of(student1ProjectWorkshop, student2ProjectWorkshop));

    student1ProjectWorkshop.setTeam(team);

    var student1InTeam = new StudentInTeamDto();
    student1InTeam.setName("student 1");

    var student2InTeam = new StudentInTeamDto();
    student2InTeam.setName("student 2");

    var expectedStudentsInTeam = List.of(student1InTeam, student2InTeam);

    when(studentProjectWorkshopMapper.toStudentInTeamDto(student1ProjectWorkshop))
        .thenReturn(student1InTeam);
    when(studentProjectWorkshopMapper.toStudentInTeamDto(student2ProjectWorkshop))
        .thenReturn(student2InTeam);

    // Act
    var gettedStudentsInTeam = studentProjectWorkshopService.getTeam(studentId);

    // Assert
    assertEquals(expectedStudentsInTeam, gettedStudentsInTeam);
  }

  @Test
  void getTeamWithStudentNotFoundException() {
    // Arrange
    var studentId = "studentId";
    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        StudentNotFoundException.class,
        () -> {
          studentProjectWorkshopService.getTeam(studentId);
        });
  }
}
