package org.example.service;

import org.example.model.dto.database.QuestionnaireDto;

public interface QuestionnaireService {

  QuestionnaireDto getQuestionnaireByStudentId(String studentId);

  void createQuestionnaire(QuestionnaireDto questionnaireDto, String studentId);

  void updateQuestionnaire(QuestionnaireDto questionnaireDto, String studentId);
}
