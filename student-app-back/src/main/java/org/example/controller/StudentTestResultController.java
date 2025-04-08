package org.example.controller;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.database.StudentTestResultDto;
import org.example.service.StudentTestResultService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test-result")
@CrossOrigin(origins = {"http://localhost:3000"})
public class StudentTestResultController {

  private final StudentTestResultService testService;

  @GetMapping("/get")
  public StudentTestResultDto getTest(Principal principal) {
    return testService.getTestResult(principal.getName());
  }
}
