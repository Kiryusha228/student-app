package org.example.mapper;

import org.example.model.dto.database.StudentInTeamDto;
import org.example.model.dto.database.StudentInfoDto;
import org.example.model.entity.StudentProjectWorkshopEntity;
import org.springframework.stereotype.Component;

@Component
public class StudentProjectWorkshopMapper {
  public StudentInfoDto toStudentInfoDto(
      StudentProjectWorkshopEntity studentProjectWorkshopEntity) {
    return new StudentInfoDto(
        studentProjectWorkshopEntity.getId(),
        studentProjectWorkshopEntity.getStudentTestResult().getTestResult(),
        studentProjectWorkshopEntity.getQuestionnaire().getExperience(),
        studentProjectWorkshopEntity.getQuestionnaire().getLanguageProficiency(),
        studentProjectWorkshopEntity.getQuestionnaire().getLanguageExperience(),
        studentProjectWorkshopEntity.getQuestionnaire().getTelegram(),
        studentProjectWorkshopEntity.getQuestionnaire().getRole());
  }

  public StudentInTeamDto toStudentInTeamDto(StudentProjectWorkshopEntity studentProjectWorkshopEntity) {
    return new StudentInTeamDto(
            studentProjectWorkshopEntity.getStudent().getName(),
            studentProjectWorkshopEntity.getQuestionnaire().getTelegram(),
            studentProjectWorkshopEntity.getQuestionnaire().getRole()
    );
  }
}
