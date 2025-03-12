package org.example.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.model.entity.StudentEntity;
import org.example.repository.StudentRepository;
import org.example.service.StudentService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

  private final StudentRepository studentRepository;

  @Override
  public List<StudentEntity> getAllStudents() {
    return studentRepository.findAll().stream().toList();
  }

  @Override
  public StudentEntity getStudentById(String studentId) {
    var student = studentRepository.findById(studentId);

    if (student.isPresent()) {
      return student.get();
    } else {
      throw new NullPointerException();
    }
  }

  @Override
  public void createStudent(StudentEntity studentEntity) {
    studentRepository.save(studentEntity);
  }

  @Override
  public void updateStudent(StudentEntity newStudentEntity) {
    var student = studentRepository.findById(newStudentEntity.getId());
    if (student.isPresent()) {
      studentRepository.save(newStudentEntity);
    } else {
      throw new NullPointerException();
    }
  }

  @Override
  public void deleteStudent(String studentId) {
    studentRepository.deleteById(studentId);
  }
}
