package org.example.mapper;

import org.example.model.dto.database.RegistrationStudentDto;
import org.example.model.dto.database.StudentDto;
import org.example.model.entity.StudentEntity;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
  public StudentEntity registrationStudentDtoToStudentEntity(
      RegistrationStudentDto registrationStudentDto, String id) {
    return new StudentEntity(
        id, registrationStudentDto.getName(), registrationStudentDto.getMail(), null);
  }

  public StudentEntity studentDtoToStudentEntity(StudentDto studentDto, String id) {
    return new StudentEntity(id, studentDto.getName(), studentDto.getMail(), null);
  }

  public StudentDto studentEntityToStudentDto(StudentEntity studentEntity) {
    return new StudentDto(studentEntity.getName(), studentEntity.getMail());
  }
}
