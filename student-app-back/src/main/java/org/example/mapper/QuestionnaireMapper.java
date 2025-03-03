package org.example.mapper;

import org.example.model.dto.QuestionnaireDto;
import org.example.model.entity.QuestionnaireEntity;
import org.example.model.entity.StudentEntity;
import org.springframework.stereotype.Component;

@Component
public class QuestionnaireMapper {
    public QuestionnaireEntity questionnaireDtoToQuestionnaireEntity(
            QuestionnaireDto questionnaireDto,
            StudentEntity studentEntity
    ) {
        return new QuestionnaireEntity(
                questionnaireDto.getId(),
                questionnaireDto.getUniversity(),
                questionnaireDto.getGraduationYear(),
                questionnaireDto.getFaculty(),
                questionnaireDto.getExperience(),
                questionnaireDto.getLanguageProficiency(),
                questionnaireDto.getLanguageExperience(),
                questionnaireDto.getTelegram(),
                questionnaireDto.getRole(),
                questionnaireDto.getGithub(),
                studentEntity
        );
    }

    public QuestionnaireDto questionnaireEntityToQuestionnaireDto(
            QuestionnaireEntity questionnaireEntity
    ) {
        return new QuestionnaireDto(
                questionnaireEntity.getId(),
                questionnaireEntity.getUniversity(),
                questionnaireEntity.getGraduationYear(),
                questionnaireEntity.getFaculty(),
                questionnaireEntity.getExperience(),
                questionnaireEntity.getLanguageProficiency(),
                questionnaireEntity.getLanguageExperience(),
                questionnaireEntity.getTelegram(),
                questionnaireEntity.getRole(),
                questionnaireEntity.getGithub(),
                questionnaireEntity.getStudent().getId()
        );
    }
}
