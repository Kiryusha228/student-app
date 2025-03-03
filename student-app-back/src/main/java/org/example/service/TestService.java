package org.example.service;

import org.example.model.dto.TestDto;

public interface TestService {
    TestDto getTestById(Long testId);

    void createTest(TestDto testDto);

    void deleteTest(Long testId);
}
