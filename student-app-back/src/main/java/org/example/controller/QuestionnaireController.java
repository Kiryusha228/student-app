package org.example.controller;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.database.QuestionnaireDto;
import org.example.service.QuestionnaireService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questionnaire")
@CrossOrigin(origins = {"http://localhost:3000"})
public class QuestionnaireController {

  private final QuestionnaireService questionnaireService;

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
}
