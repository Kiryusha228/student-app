package org.example.controller;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.example.mapper.QuestionnaireMapper;
import org.example.model.dto.QuestionnaireDto;
import org.example.service.QuestionnaireService;
import org.example.service.StudentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questionnaire")
public class QuestionnaireController {

  private final QuestionnaireService questionnaireService;
  private final StudentService studentService;
  private final QuestionnaireMapper questionnaireMapper;

  @PostMapping("/add")
  public void addQuestionnaire(
      Principal principal, @RequestBody QuestionnaireDto questionnaireDto) {
    questionnaireService.createQuestionnaire(questionnaireDto, principal.getName());
  }

  @GetMapping("/get")
  public QuestionnaireDto getQuestionnaire(Principal principal) {
    return questionnaireService.getQuestionnaireByStudentId(principal.getName());
  }

  @PatchMapping("/update")
  public void updateQuestionnaire(
      Principal principal, @RequestBody QuestionnaireDto questionnaireDto) {
    questionnaireService.updateQuestionnaire(questionnaireDto, principal.getName());
  }

  @DeleteMapping("/delete")
  public void deleteQuestionnaire(Principal principal) {
    questionnaireService.deleteQuestionnaire(principal.getName());
  }
}
