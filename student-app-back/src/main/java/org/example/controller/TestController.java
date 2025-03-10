package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.TestDto;
import org.example.service.TestService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("test")
public class TestController {

    private final TestService testService;

    @PostMapping("/add")
    public void addTest(Principal principal, @RequestBody TestDto testDto) {
        testService.createTest(testDto, principal.getName());
    }

    @GetMapping("/get")
    public TestDto getTest(Principal principal) {
        return testService.getTest(principal.getName());
    }

    @DeleteMapping("/delete")
    public void deleteQuestionnaire(Principal principal) {
        testService.deleteTest(principal.getName());
    }
}
