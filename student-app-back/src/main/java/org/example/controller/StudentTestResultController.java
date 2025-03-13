package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.StudentTestResultDto;
import org.example.service.StudentTestResultService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("test")
public class StudentTestResultController {

    private final StudentTestResultService testService;

    @PostMapping("/add")
    public void addTest(Principal principal, @RequestBody StudentTestResultDto testDto) {
        testService.createTest(testDto, principal.getName());
    }

    @GetMapping("/get")
    public StudentTestResultDto getTest(Principal principal) {
        return testService.getTest(principal.getName());
    }

    @DeleteMapping("/delete")
    public void deleteQuestionnaire(Principal principal) {
        testService.deleteTest(principal.getName());
    }
}
