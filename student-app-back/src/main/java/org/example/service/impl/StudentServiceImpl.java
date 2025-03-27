package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.exception.StudentNotFoundException;
import org.example.mapper.StudentMapper;
import org.example.model.entity.StudentEntity;
import org.example.repository.QuestionnaireRepository;
import org.example.repository.StudentRepository;
import org.example.repository.StudentTestResultRepository;
import org.example.service.StudentService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

  private final StudentRepository studentRepository;
  private final StudentTestResultRepository studentTestResultRepository;
  private final QuestionnaireRepository questionnaireRepository;

  private final StudentMapper studentMapper;

  @Override
  public StudentEntity getStudentById(String studentId) {
    var student = studentRepository.findById(studentId);

    if (student.isEmpty()) {
      throw new StudentNotFoundException("Студент не найден");
    }

    return student.get();
  }

  @Override
  public void createStudent(StudentEntity studentEntity) {
    studentRepository.save(studentEntity);
  }

  @Override
  public void updateStudent(StudentEntity newStudentEntity) {
    var student = studentRepository.findById(newStudentEntity.getId());

    if (student.isEmpty()) {
      throw new StudentNotFoundException("Студент не найден");
    }

    studentRepository.save(newStudentEntity);
  }

  @Override
  public void deleteStudent(String studentId) {
    studentRepository.deleteById(studentId);
  }
}
