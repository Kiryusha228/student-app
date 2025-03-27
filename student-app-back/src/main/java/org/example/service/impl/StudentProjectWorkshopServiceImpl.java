package org.example.service.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.exception.ProjectWorkshopNotFoundException;
import org.example.exception.StudentNotFoundException;
import org.example.mapper.StudentProjectWorkshopMapper;
import org.example.model.dto.database.StudentInfoDto;
import org.example.model.entity.QuestionnaireEntity;
import org.example.model.entity.StudentProjectWorkshopEntity;
import org.example.model.entity.StudentTestResultEntity;
import org.example.repository.ProjectWorkshopRepository;
import org.example.repository.StudentProjectWorkshopRepository;
import org.example.repository.StudentRepository;
import org.example.service.ProjectWorkshopService;
import org.example.service.StudentProjectWorkshopService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentProjectWorkshopServiceImpl implements StudentProjectWorkshopService {
  private final StudentRepository studentRepository;
  private final ProjectWorkshopRepository projectWorkshopRepository;
  private final StudentProjectWorkshopRepository studentProjectWorkshopRepository;
  private final ProjectWorkshopService projectWorkshopService;
  private final StudentProjectWorkshopMapper studentProjectWorkshopMapper;

  @Override
  public List<StudentInfoDto> getAllStudents() {
    var lastProjectWorkshopId = projectWorkshopService.getLastProjectWorkshopId();
    var students =
        studentProjectWorkshopRepository.findAll().stream()
            .filter(student -> lastProjectWorkshopId.equals(student.getProjectWorkshop().getId()))
            .filter(student -> student.getStudentTestResult() != null)
            .filter(student -> student.getQuestionnaire() != null)
            .toList();

    var allStudentsInfo = new ArrayList<StudentInfoDto>();

    for (var student : students) {
      allStudentsInfo.add(studentProjectWorkshopMapper.toStudentInfoDto(student));
    }

    return allStudentsInfo;
  }

  @Override
  public void setTestResult(
      StudentProjectWorkshopEntity studentProjectWorkshop, StudentTestResultEntity testResult) {
    studentProjectWorkshop.setStudentTestResult(testResult);
    studentProjectWorkshopRepository.save(studentProjectWorkshop);
  }

  @Override
  public void setQuestionnaire(
      StudentProjectWorkshopEntity studentProjectWorkshop, QuestionnaireEntity questionnaire) {
    studentProjectWorkshop.setQuestionnaire(questionnaire);
    studentProjectWorkshopRepository.save(studentProjectWorkshop);
  }

  @Override
  public void createStudentProjectWorkshop(String studentId) {
    var student = studentRepository.findById(studentId);

    if (student.isEmpty()) {
      throw new StudentNotFoundException("Такого студента не существует");
    }

    var projectWorkshopId = projectWorkshopService.getLastProjectWorkshopId();
    var projectWorkshop = projectWorkshopRepository.findById(projectWorkshopId);

    if (projectWorkshop.isEmpty()) {
      throw new ProjectWorkshopNotFoundException("Такой мастерской не существует");
    }

    var studentProjectWorkshop =
        new StudentProjectWorkshopEntity(0L, student.get(), projectWorkshop.get(), null, null);

    var savedStudentProjectWorkshop = studentProjectWorkshopRepository.save(studentProjectWorkshop);
    projectWorkshopService.addStudent(projectWorkshopId, savedStudentProjectWorkshop);
  }
}
