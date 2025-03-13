package org.example.service;

import org.example.model.dto.StudentTestResultDto;

public interface StudentTestResultService {
    StudentTestResultDto getTest(String studentId);

    void createTest(StudentTestResultDto testDto, String studentId);

    void deleteTest(String studentId);
}
