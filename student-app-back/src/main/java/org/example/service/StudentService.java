package org.example.service;

import org.example.model.entity.StudentEntity;

import java.util.List;

public interface StudentService {
    List<StudentEntity> getAllStudents();

    StudentEntity getStudentById(String studentId);

    void createStudent(StudentEntity studentEntity);

    void updateStudent(StudentEntity studentEntity);

    void deleteStudent(String studentId);
}
