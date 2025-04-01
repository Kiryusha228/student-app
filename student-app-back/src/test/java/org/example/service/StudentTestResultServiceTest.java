package org.example.service;

import org.example.exception.StudentNotFoundException;
import org.example.exception.StudentTestResultAlreadyExistException;
import org.example.exception.StudentTestResultNotFoundException;
import org.example.mapper.StudentTestResultMapper;
import org.example.model.dto.database.QuestionnaireDto;
import org.example.model.dto.database.StudentTestResultDto;
import org.example.model.entity.QuestionnaireEntity;
import org.example.model.entity.StudentEntity;
import org.example.model.entity.StudentProjectWorkshopEntity;
import org.example.model.entity.StudentTestResultEntity;
import org.example.repository.StudentRepository;
import org.example.repository.StudentTestResultRepository;
import org.example.service.impl.StudentTestResultImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentTestResultServiceTest {

    @Mock
    private StudentTestResultRepository testRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private StudentTestResultMapper testMapper;
    @InjectMocks
    private StudentTestResultImpl studentTestResultService;

    @Test
    void getTestResult() {
        //Arrange
        var studentId = "studentId";

        var studentTestResultDto = new StudentTestResultDto();
        studentTestResultDto.setTestResult(10);

        var studentTestResultEntity = new StudentTestResultEntity();
        studentTestResultEntity.setId(1L);
        studentTestResultEntity.setTestResult(10);

        var studentProjectWorkshop = new StudentProjectWorkshopEntity();
        studentProjectWorkshop.setStudentTestResult(studentTestResultEntity);

        var student = new StudentEntity(
                studentId,
                "student",
                "student@mail",
                List.of(studentProjectWorkshop)
        );

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        when(testMapper.testEntityToTestDto(studentTestResultEntity))
                .thenReturn(studentTestResultDto);

        //Act
        var gettedStudentTestResult = studentTestResultService.getTestResult(studentId);

        //Assert
        assertEquals(studentTestResultDto, gettedStudentTestResult);
    }

    @Test
    void getTestResultWithStudentNotFoundException() {
        //Arrange
        var studentId = "studentId";
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
        //Act & Assert
        assertThrows(StudentNotFoundException.class, () ->
                studentTestResultService.getTestResult(studentId)
        );
    }

    @Test
    void getTestResultWithStudentTestResultNotFoundException() {
        //Arrange
        var studentId = "studentId";

        var studentTestResultDto = new StudentTestResultDto();
        studentTestResultDto.setTestResult(10);

        var studentTestResultEntity = new StudentTestResultEntity();
        studentTestResultEntity.setId(1L);
        studentTestResultEntity.setTestResult(10);

        var studentProjectWorkshop = new StudentProjectWorkshopEntity();

        var student = new StudentEntity(
                studentId,
                "student",
                "student@mail",
                List.of(studentProjectWorkshop)
        );

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        //Act & Assert
        assertThrows(StudentTestResultNotFoundException.class, () ->
                studentTestResultService.getTestResult(studentId)
        );
    }

    @Test
    void createTestResult() {
        //Arrange
        var studentId = "studentId";

        var studentTestResultDto = new StudentTestResultDto();
        studentTestResultDto.setTestResult(10);

        var studentTestResultEntity = new StudentTestResultEntity();
        studentTestResultEntity.setId(1L);
        studentTestResultEntity.setTestResult(10);

        var studentProjectWorkshop = new StudentProjectWorkshopEntity();

        var student = new StudentEntity(
                studentId,
                "student",
                "student@mail",
                List.of(studentProjectWorkshop)
        );

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(testRepository.findByStudentProjectWorkshop(studentProjectWorkshop)).thenReturn(Optional.empty());
        when(testMapper.testDtoToTestEntity(studentTestResultDto, studentProjectWorkshop, 0L))
                .thenReturn(studentTestResultEntity);
        when(testRepository.save(studentTestResultEntity)).thenReturn(studentTestResultEntity);

        //Act
        studentTestResultService.createTestResult(studentTestResultDto, studentId);

        //Assert
        verify(testRepository).save(studentTestResultEntity);
        assertEquals(studentProjectWorkshop.getStudentTestResult(), studentTestResultEntity);
    }

    @Test
    void createTestResultWithStudentNotFoundException() {
        //Arrange
        var studentId = "studentId";
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        var studentTestResultDto = new StudentTestResultDto();
        studentTestResultDto.setTestResult(10);

        //Act & Assert
        assertThrows(StudentNotFoundException.class, () ->
                studentTestResultService.createTestResult(studentTestResultDto, studentId)
        );
    }

    @Test
    void createTestResultWithStudentTestResultAlreadyExistException() {
        //Arrange
        var studentId = "studentId";

        var studentTestResultDto = new StudentTestResultDto();
        studentTestResultDto.setTestResult(10);

        var studentTestResultEntity = new StudentTestResultEntity();
        studentTestResultEntity.setId(1L);
        studentTestResultEntity.setTestResult(10);

        var studentProjectWorkshop = new StudentProjectWorkshopEntity();

        var student = new StudentEntity(
                studentId,
                "student",
                "student@mail",
                List.of(studentProjectWorkshop)
        );

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(testRepository.findByStudentProjectWorkshop(studentProjectWorkshop)).thenReturn(Optional.of(studentTestResultEntity));

        //Act & Assert
        assertThrows(StudentTestResultAlreadyExistException.class, () ->
                studentTestResultService.createTestResult(studentTestResultDto, studentId)
        );
    }
}