package org.example.service;

import org.example.model.dto.StudentTestResultDto;

public interface StudentTestResultService {
    StudentTestResultDto getTestResult(String studentId);

    void createTestResult(StudentTestResultDto testDto, String studentId);

    void deleteTestResult(String studentId);
}
