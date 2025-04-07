package org.example.service;

import org.example.model.entity.StudentEntity;

public interface StudentService {

  StudentEntity getStudentById(String studentId);

  void createStudent(StudentEntity studentEntity);

  void updateStudent(StudentEntity studentEntity);

  void deleteStudent(String studentId);

  Boolean checkRegistration(String userMail);
}
