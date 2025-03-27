package org.example.service;

import org.example.model.dto.database.StudentTestResultDto;

public interface StudentTestResultService {
  StudentTestResultDto getTestResult(String studentId);

  void createTestResult(StudentTestResultDto testDto, String studentId);
}
