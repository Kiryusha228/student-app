package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.database.StudentTestResultDto;
import org.example.service.StudentTestResultService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test-result")
public class StudentTestResultController {

    private final StudentTestResultService testService;

    @PostMapping("/add")
    public void addTest(Principal principal, @RequestBody StudentTestResultDto testDto) {
        testService.createTestResult(testDto, principal.getName());
    }

    @GetMapping("/get")
    public StudentTestResultDto getTest(Principal principal) {
        return testService.getTestResult(principal.getName());
    }

    @DeleteMapping("/delete")
    public void deleteQuestionnaire(Principal principal) {
        testService.deleteTestResult(principal.getName());
    }
}
