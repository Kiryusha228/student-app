package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.QuestionnaireDto;
import org.example.service.QuestionnaireService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("questionnaire")
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;

    @PostMapping("/add")
    public void addQuestionnaire(@RequestBody QuestionnaireDto questionnaireDto) {
        questionnaireService.createQuestionnaire(questionnaireDto);
    }

    @GetMapping("/get")
    public QuestionnaireDto getQuestionnaire(Long questionnaireId) {
        return questionnaireService.getQuestionnaireById(questionnaireId);
    }

    @PatchMapping("/update")
    public void updateQuestionnaire(@RequestBody QuestionnaireDto questionnaireDto) {
        questionnaireService.updateQuestionnaire(questionnaireDto);
    }

    @DeleteMapping("/delete")
    public void deleteQuestionnaire(Long questionnaireId) {
        questionnaireService.deleteQuestionnaire(questionnaireId);
    }
}
