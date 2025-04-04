package org.example.mapper;

import org.example.model.dto.database.QuestionnaireDto;
import org.example.model.entity.QuestionnaireEntity;
import org.example.model.entity.StudentProjectWorkshopEntity;
import org.springframework.stereotype.Component;

@Component
public class QuestionnaireMapper {
  public QuestionnaireEntity toQuestionnaireEntity(
      QuestionnaireDto questionnaireDto,
      StudentProjectWorkshopEntity studentProjectWorkshopEntity,
      Long questionnaireId) {
    return new QuestionnaireEntity(
        questionnaireId,
        questionnaireDto.getUniversity(),
        questionnaireDto.getGraduationYear(),
        questionnaireDto.getFaculty(),
        questionnaireDto.getExperience(),
        questionnaireDto.getLanguageProficiency(),
        questionnaireDto.getLanguageExperience(),
        questionnaireDto.getTelegram(),
        questionnaireDto.getRole(),
        questionnaireDto.getGithub(),
        studentProjectWorkshopEntity);
  }

  public QuestionnaireDto toQuestionnaireDto(QuestionnaireEntity questionnaireEntity) {
    return new QuestionnaireDto(
        questionnaireEntity.getUniversity(),
        questionnaireEntity.getGraduationYear(),
        questionnaireEntity.getFaculty(),
        questionnaireEntity.getExperience(),
        questionnaireEntity.getLanguageProficiency(),
        questionnaireEntity.getLanguageExperience(),
        questionnaireEntity.getTelegram(),
        questionnaireEntity.getRole(),
        questionnaireEntity.getGithub());
  }
}
