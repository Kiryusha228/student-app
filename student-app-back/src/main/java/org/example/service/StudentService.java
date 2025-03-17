package org.example.service;

import java.util.List;

import org.example.model.dto.database.StudentInfoDto;
import org.example.model.entity.StudentEntity;

public interface StudentService {
  List<StudentInfoDto> getAllStudents();

  StudentEntity getStudentById(String studentId);

  void createStudent(StudentEntity studentEntity);

  void updateStudent(StudentEntity studentEntity);

  void deleteStudent(String studentId);
}
