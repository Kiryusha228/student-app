package org.example.service;

import java.util.List;
import org.example.model.entity.TestEntity;

public interface TestService {

  void createTest(List<TestEntity> test);

  List<TestEntity> getTest();

  int checkAnswers(List<Integer> answers);

  void deleteTest();
}
