package org.example.service;

import org.example.exception.TestNotFoundException;
import org.example.model.entity.TestEntity;
import org.example.repository.TestRepository;
import org.example.service.impl.TestServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestServiceTest {

    @Mock
    private TestRepository testRepository;

    @InjectMocks
    private TestServiceImpl testService;

    @Test
    void createTest() {
        //Arrange
        var test = new TestEntity();
        test.setId(1L);
        var testList = List.of(test);
        //Act
        testService.createTest(testList);
        //Assert
        verify(testRepository).saveAll(testList);
    }

    @Test
    void getTest() {
        //Arrange
        var test = new TestEntity();
        test.setId(1L);
        var testList = List.of(test);
        when(testRepository.findAll()).thenReturn(testList);
        //Act
        var gettedTest = testService.getTest();
        //Assert
        assertEquals(testList, gettedTest);
    }

    @Test
    void checkAnswers() {
        //Arrange
        var test1 = new TestEntity();
        test1.setId(1L);
        test1.setRightAnswer(1);

        var test2 = new TestEntity();
        test2.setId(2L);
        test2.setRightAnswer(2);

        var testList = List.of(test1, test2);

        var answers = List.of(1, 1);

        when(testRepository.findAll()).thenReturn(testList);
        var expectedTestResult = 1;
        //Act
        var gettedTestResult = testService.checkAnswers(answers);
        //Assert
        assertEquals(expectedTestResult, gettedTestResult);
    }

    @Test
    void checkAnswersWithTestNotFoundException() {
        //Arrange
        var answers = List.of(1, 1);

        when(testRepository.findAll()).thenReturn(new ArrayList<TestEntity>());
        //Act & Assert
        assertThrows(TestNotFoundException.class, () ->
                testService.checkAnswers(answers)
        );
    }

    @Test
    void deleteTest() {
        //Act
        testService.deleteTest();
        //Assert
        verify(testRepository).deleteAll();
    }
}