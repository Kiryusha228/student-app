package org.example.service;

import org.example.model.dto.TestDto;

public interface TestService {
  TestDto getTest(String studentId);

  void createTest(TestDto testDto, String studentId);

  void deleteTest(String studentId);
}
