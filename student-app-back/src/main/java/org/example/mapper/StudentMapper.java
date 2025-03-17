package org.example.mapper;

import org.example.model.dto.database.RegistrationStudentDto;
import org.example.model.dto.database.StudentDto;
import org.example.model.dto.database.StudentInfoDto;
import org.example.model.entity.QuestionnaireEntity;
import org.example.model.entity.StudentEntity;
import org.example.model.entity.StudentTestResultEntity;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
  public StudentEntity registrationStudentDtoToStudentEntity(
      RegistrationStudentDto registrationStudentDto, String id) {
    return new StudentEntity(
        id, registrationStudentDto.getName(), registrationStudentDto.getMail());
  }

  public StudentEntity studentDtoToStudentEntity(StudentDto studentDto, String id) {
    return new StudentEntity(id, studentDto.getName(), studentDto.getMail());
  }

  public StudentDto studentEntityToStudentDto(StudentEntity studentEntity) {
    return new StudentDto(studentEntity.getName(), studentEntity.getMail());
  }

  public StudentInfoDto toStudentInfoDto(
          StudentEntity studentEntity,
          QuestionnaireEntity questionnaireEntity,
          StudentTestResultEntity studentTestResultEntity){
    return new StudentInfoDto(
            studentEntity.getName(),
            studentTestResultEntity.getTestResult(),
            questionnaireEntity.getExperience(),
            questionnaireEntity.getLanguageProficiency(),
            questionnaireEntity.getLanguageExperience(),
            questionnaireEntity.getTelegram(),
            questionnaireEntity.getRole());
  }
}
