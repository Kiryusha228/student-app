package org.example.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.exception.StudentNotFoundException;
import org.example.exception.StudentTestResultAlreadyExistException;
import org.example.exception.StudentTestResultNotFoundException;
import org.example.mapper.StudentTestResultMapper;
import org.example.model.dto.database.StudentTestResultDto;
import org.example.repository.StudentRepository;
import org.example.repository.StudentTestResultRepository;
import org.example.service.StudentTestResultService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentTestResultImpl implements StudentTestResultService {

  private final StudentTestResultRepository testRepository;
  private final StudentRepository studentRepository;
  private final StudentTestResultMapper testMapper;

  @Override
  public StudentTestResultDto getTestResult(String studentId) {
    var student = studentRepository.findById(studentId);

    if (student.isEmpty()) {
      throw new StudentNotFoundException("Студент не найден");
    }

    var studentProjectWorkshops = student.get().getStudentProjectWorkshop();
    var lastStudentProjectWorkshop =
        studentProjectWorkshops.get(studentProjectWorkshops.size() - 1);
    var testResult = lastStudentProjectWorkshop.getStudentTestResult();

    if (testResult == null) {
      throw new StudentTestResultNotFoundException("У данного студента нет результатов теста");
    }

    return testMapper.testEntityToTestDto(testResult);
  }

  @Override
  @Transactional
  public void createTestResult(StudentTestResultDto testDto, String studentId) {
    var student = studentRepository.findById(studentId);

    if (student.isEmpty()) {
      throw new StudentNotFoundException("Студент не найден");
    }

    var studentProjectWorkshops = student.get().getStudentProjectWorkshop();
    var lastStudentProjectWorkshop =
        studentProjectWorkshops.get(studentProjectWorkshops.size() - 1);

    var testResult = testRepository.findByStudentProjectWorkshop(lastStudentProjectWorkshop);

    if (testResult.isPresent()) {
      throw new StudentTestResultAlreadyExistException("Данный студент уже решал тест");
    }

    var savedTestResult =
        testRepository.save(
            testMapper.testDtoToTestEntity(testDto, lastStudentProjectWorkshop, 0L));

    lastStudentProjectWorkshop.setStudentTestResult(savedTestResult);
  }
}
