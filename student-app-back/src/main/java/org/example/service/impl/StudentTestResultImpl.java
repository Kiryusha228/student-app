package org.example.service.impl;

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

    var testResult = testRepository.findByStudent(student.get());

    if (testResult.isEmpty()) {
      throw new StudentTestResultNotFoundException("У данного студента нет результатов теста");
    }

    return testMapper.testEntityToTestDto(testResult.get());
  }

  @Override
  public void createTestResult(StudentTestResultDto testDto, String studentId) {
    var student = studentRepository.findById(studentId);

    if (student.isEmpty()) {
      throw new StudentNotFoundException("Студент не найден");
    }

    if (testRepository.findByStudent(student.get()).isPresent()) {
      throw new StudentTestResultAlreadyExistException("Данный студент уже решал тест");
    }

    testRepository.save(testMapper.testDtoToTestEntity(testDto, student.get(), 0L));
  }

  @Override
  public void deleteTestResult(String studentId) {
    var student = studentRepository.findById(studentId);

    if (student.isEmpty()) {
      throw new StudentNotFoundException("Студент не найден");
    }

    var testResult = testRepository.findByStudent(student.get());

    if (testResult.isEmpty()) {
      throw new StudentTestResultNotFoundException("У данного студента нет результатов теста");
    }

    testRepository.delete(testResult.get());
  }
}
