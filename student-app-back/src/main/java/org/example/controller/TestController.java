package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.StudentTestResultDto;
import org.example.model.entity.TestEntity;
import org.example.service.StudentTestResultService;
import org.example.service.TestService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestController {
    private final TestService testService;
    private final StudentTestResultService studentTestResultService;

    @PostMapping("/add")
    public void addTest(@RequestBody List<TestEntity> test) {
        testService.createTest(test);
    }

    @GetMapping("/get")
    public List<TestEntity> getTest() {
        return testService.getTest();
    }

    @PostMapping("/check")
    public void checkAnswersAndSetToStudentTestResult(Principal principal, @RequestBody List<Integer> answers) {
        studentTestResultService.createTestResult(
                new StudentTestResultDto(
                        testService.checkAnswers(answers)), principal.getName());
    }

    @DeleteMapping("/delete")
    public void deleteTest() {
        testService.deleteTest();
    }

}
