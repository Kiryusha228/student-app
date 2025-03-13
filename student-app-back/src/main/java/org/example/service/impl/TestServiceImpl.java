package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.entity.TestEntity;
import org.example.repository.TestRepository;
import org.example.service.TestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;

    @Override
    public void createTest(List<TestEntity> test) {
        testRepository.saveAll(test);
    }

    @Override
    public List<TestEntity> getTest() {
        return testRepository.findAll();
    }

    @Override
    public int checkAnswers(List<Integer> answers) {
        var test = testRepository.findAll();
        var testResult = 0;

        for (int i = 0; i < test.size(); i++) {
            if (test.get(i).getRightAnswer() == answers.get(i)) {
                testResult++;
            }
        }

        return testResult;
    }

    @Override
    public void deleteTest() {
        testRepository.deleteAll();
    }
}
