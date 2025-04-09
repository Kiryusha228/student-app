package org.example.service.impl;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.exception.ProjectWorkshopNotFoundException;
import org.example.exception.QuestionnaireNotFoundException;
import org.example.exception.StudentNotFoundException;
import org.example.exception.StudentProjectWorkshopNotFoundException;
import org.example.mapper.StudentProjectWorkshopMapper;
import org.example.model.dto.database.StudentInTeamDto;
import org.example.model.dto.database.StudentInfoDto;
import org.example.model.entity.StudentProjectWorkshopEntity;
import org.example.repository.ProjectWorkshopRepository;
import org.example.repository.QuestionnaireRepository;
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
  private final QuestionnaireRepository questionnaireRepository;

  @Override
  public List<StudentInfoDto> getAllStudentsByProjectWorkshopId(Long projectWorkshopId) {
    var students =
        studentProjectWorkshopRepository.findAll().stream()
            .filter(student -> projectWorkshopId.equals(student.getProjectWorkshop().getId()))
            .toList();

    var studentsInfo = new ArrayList<StudentInfoDto>();

    for (var student : students) {
      studentsInfo.add(studentProjectWorkshopMapper.toStudentInfoDto(student));
    }

    return studentsInfo;
  }

  @Override
  public List<StudentInfoDto> getAllPastStudents() {
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
  public StudentProjectWorkshopEntity getStudentProjectWorkshopById(Long studentProjectWorkshopId) {
    var studentProjectWorkshop =
        studentProjectWorkshopRepository.findById(studentProjectWorkshopId);
    if (studentProjectWorkshop.isEmpty()) {
      throw new StudentProjectWorkshopNotFoundException("Такого студента в мастерской нет");
    }
    return studentProjectWorkshop.get();
  }

  @Override
  @Transactional
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
        new StudentProjectWorkshopEntity(
            0L, student.get(), projectWorkshop.get(), null, null, null);

    var savedStudentProjectWorkshop = studentProjectWorkshopRepository.save(studentProjectWorkshop);
    projectWorkshopService.addStudent(projectWorkshopId, savedStudentProjectWorkshop);
  }

  @Override
  public List<StudentInTeamDto> getTeam(String studentId) {

    // todo добавить метод получения последнего студента в мастерской
    var student = studentRepository.findById(studentId);

    if (student.isEmpty()) {
      throw new StudentNotFoundException("Студент не найден");
    }

    var studentProjectWorkshops = student.get().getStudentProjectWorkshop();
    var lastStudentProjectWorkshop =
        studentProjectWorkshops.get(studentProjectWorkshops.size() - 1);

    var studentsProjectWorkshopInTeam =
        lastStudentProjectWorkshop.getTeam().getStudentProjectWorkshop();

    var studentsInTeam = new ArrayList<StudentInTeamDto>();

    for (var studentInTeam : studentsProjectWorkshopInTeam) {
      studentsInTeam.add(studentProjectWorkshopMapper.toStudentInTeamDto(studentInTeam));
    }

    return studentsInTeam;
  }

  @Override
  public StudentInfoDto getStudentInfoById(Long id) {
    var student = studentProjectWorkshopRepository.findById(id);
    if (student.isEmpty()) {
      throw new StudentProjectWorkshopNotFoundException("Студент не найден");
    }
    return studentProjectWorkshopMapper.toStudentInfoDto(student.get());
  }

  @Override
  public StudentInfoDto getStudentInfoByTelegram(String telegram) {
    var questionnaire = questionnaireRepository.findByTelegram(telegram);
    if (questionnaire.isEmpty()) {
      throw new QuestionnaireNotFoundException(
          "Анкета студента по указанному телеграмму не найдена");
    }
    return studentProjectWorkshopMapper.toStudentInfoDto(
        questionnaire.get().getStudentProjectWorkshop());
  }

  @Override
  public StudentInfoDto getStudentInfoByName(String name) {
    var student = studentRepository.findByName(name);
    if (student.isEmpty()) {
      throw new StudentNotFoundException("Студент не найден");
    }

    var studentsProjectWorkshop = student.get().getStudentProjectWorkshop();

    return studentProjectWorkshopMapper.toStudentInfoDto(
        studentsProjectWorkshop.get(studentsProjectWorkshop.size() - 1));
  }

  @Override
  public List<StudentInfoDto> getStudentsInfoByName(String name) {
    var students = studentProjectWorkshopRepository.findAll().stream()
            .filter(student -> student.getStudent().getName().toLowerCase().contains(name.toLowerCase()))
            .toList();

    if (students.isEmpty()) {
      throw new StudentNotFoundException("Студент не найден");
    }

    var allStudentsInfo = new ArrayList<StudentInfoDto>();

    for (var student : students) {
      allStudentsInfo.add(studentProjectWorkshopMapper.toStudentInfoDto(student));
    }

    return allStudentsInfo;
  }

  @Override
  public Boolean checkStudentRegistrationOnProjectWorkshop(String studentId, Long projectWorkshopId) {

    var projectWorkshop = projectWorkshopRepository.findById(projectWorkshopId).get();

    var student = studentRepository.findById(studentId).get();

    var size = student.getStudentProjectWorkshop().size();
    if (size == 0){
      return false;
    }

    return student.getStudentProjectWorkshop().get(size - 1).getProjectWorkshop() == projectWorkshop;
  }
}
