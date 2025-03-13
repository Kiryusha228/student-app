package org.example.service;

import org.example.model.entity.TestEntity;

import java.util.List;

public interface TestService {

    void createTest(List<TestEntity> test);

    List<TestEntity> getTest();

    int checkAnswers(List<Integer> answers);

    void deleteTest();
}

