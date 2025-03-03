package org.example.service;

import org.example.model.dto.QuestionnaireDto;

public interface QuestionnaireService {

    QuestionnaireDto getQuestionnaireById(Long questionnaireId);

    void createQuestionnaire(QuestionnaireDto questionnaireDto);

    void updateQuestionnaire(QuestionnaireDto questionnaireDto);

    void deleteQuestionnaire(Long questionnaireId);
}
