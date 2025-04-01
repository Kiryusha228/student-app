package org.example.service;

import org.example.exception.StudentNotFoundException;
import org.example.model.entity.StudentEntity;
import org.example.repository.StudentRepository;
import org.example.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void getStudentById() {
        //Arrange
        var studentId = "studentId";
        var student = new StudentEntity();
        student.setId(studentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        //Act
        var gettedStudent = studentService.getStudentById(studentId);

        //Assert
        assertEquals(student, gettedStudent);
    }

    @Test
    void getStudentByIdWithStudentNotFoundException() {
        //Arrange
        var studentId = "studentId";
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
        //Act & Assert
        assertThrows(StudentNotFoundException.class, () ->
                studentService.getStudentById(studentId)
        );
    }

    @Test
    void createStudent() {
        //Arrange
        var student = new StudentEntity();
        student.setId("studentId");
        //Act
        studentService.createStudent(student);
        //Assert
        verify(studentRepository).save(student);
    }

    @Test
    void updateStudent() {
        //Arrange
        var student = new StudentEntity();
        student.setId("studentId");
        student.setName("student name");

        var newStudent = new StudentEntity();
        newStudent.setId("studentId");
        newStudent.setName("new student name");

        when(studentRepository.findById(newStudent.getId())).thenReturn(Optional.of(student));
        //Act
        studentService.updateStudent(newStudent);
        //Assert
        verify(studentRepository).save(newStudent);
    }

    @Test
    void updateStudentWithStudentNotFoundException() {
        //Arrange
        var newStudent = new StudentEntity();
        newStudent.setId("studentId");

        when(studentRepository.findById(newStudent.getId())).thenReturn(Optional.empty());
        //Act & Assert
        assertThrows(StudentNotFoundException.class, () ->
                studentService.updateStudent(newStudent)
        );
    }

    @Test
    void deleteStudent() {
        //Arrange
        var studentId = "studentId";
        //Act
        studentService.deleteStudent(studentId);
        //Assert
        verify(studentRepository).deleteById(studentId);
    }
}