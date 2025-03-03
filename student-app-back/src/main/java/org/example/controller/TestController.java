package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.TestDto;
import org.example.service.TestService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("test")
public class TestController {

    private final TestService testService;

    @PostMapping("/add")
    public void addTest(@RequestBody TestDto testDto) {
        testService.createTest(testDto);
    }

    @GetMapping("/get")
    public TestDto getQuestionnaire(Long testId) {
        return testService.getTestById(testId);
    }

    @DeleteMapping("/delete")
    public void deleteQuestionnaire(Long testId) {
        testService.deleteTest(testId);
    }
}
