package org.example.service;

import org.example.exception.ProjectWorkshopNotFoundException;
import org.example.exception.QuestionnaireNotFoundException;
import org.example.exception.StudentNotFoundException;
import org.example.mapper.QuestionnaireMapper;
import org.example.model.dto.database.QuestionnaireDto;
import org.example.model.entity.ProjectWorkshopEntity;
import org.example.model.entity.QuestionnaireEntity;
import org.example.model.entity.StudentEntity;
import org.example.model.entity.StudentProjectWorkshopEntity;
import org.example.repository.QuestionnaireRepository;
import org.example.repository.StudentRepository;
import org.example.service.impl.QuestionnaireServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionnaireServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private QuestionnaireRepository questionnaireRepository;

    @Mock
    private QuestionnaireMapper questionnaireMapper;

    @InjectMocks
    private QuestionnaireServiceImpl questionnaireService;

    @Test
    void getQuestionnaireByStudentId() {
        //Arrange
        var studentId = "studentId";

        var questionnaireDto = new QuestionnaireDto();
        questionnaireDto.setExperience("experience");

        var questionnaireEntity = new QuestionnaireEntity();
        questionnaireEntity.setId(1L);
        questionnaireEntity.setExperience("experience");

        var studentProjectWorkshop = new StudentProjectWorkshopEntity();
        studentProjectWorkshop.setQuestionnaire(questionnaireEntity);

        var student = new StudentEntity(
                studentId,
                "student",
                "student@mail",
                List.of(studentProjectWorkshop)
        );

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        when(questionnaireMapper.toQuestionnaireDto(questionnaireEntity))
                .thenReturn(questionnaireDto);

        //Act
        var gettedQuestionnaire = questionnaireService.getQuestionnaireByStudentId(studentId);

        //Assert
        assertEquals(questionnaireDto, gettedQuestionnaire);
    }

    @Test
    void getQuestionnaireByStudentIdWithStudentNotFoundException() {

        //Arrange
        var studentId = "studentId";

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        //Agt & Assert
        assertThrows(StudentNotFoundException.class, () -> {
            questionnaireService.getQuestionnaireByStudentId(studentId);
        });
    }

    @Test
    void getQuestionnaireByStudentIdWithQuestionnaireNotFoundException() {

        //Arrange
        var studentId = "studentId";

        var studentEntity = new StudentEntity();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentEntity));

        studentEntity.setStudentProjectWorkshop(List.of(new StudentProjectWorkshopEntity()));

        //Agt & Assert
        assertThrows(QuestionnaireNotFoundException.class, () -> {
            questionnaireService.getQuestionnaireByStudentId(studentId);
        });
    }

    @Test
    void createQuestionnaire() {
        //Arrange
        var studentId = "studentId";

        var studentProjectWorkshop = new StudentProjectWorkshopEntity();

        var student = new StudentEntity(
                studentId,
                "student",
                "student@mail",
                List.of(studentProjectWorkshop)
        );

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        var questionnaireDto = new QuestionnaireDto();
        questionnaireDto.setExperience("experience");

        var questionnaireEntity = new QuestionnaireEntity();
        questionnaireEntity.setId(1L);
        questionnaireEntity.setExperience("experience");

        when(questionnaireMapper.toQuestionnaireEntity(questionnaireDto, studentProjectWorkshop, 0L))
                .thenReturn(questionnaireEntity);

        when(questionnaireRepository.save(questionnaireEntity)).thenReturn(questionnaireEntity);

        //Act
        questionnaireService.createQuestionnaire(questionnaireDto, studentId);

        //Assert
        verify(questionnaireRepository).save(questionnaireEntity);
        assertEquals(questionnaireEntity, studentProjectWorkshop.getQuestionnaire());
    }

    @Test
    void createQuestionnaireWithStudentNotFoundException() {
        //Arrange
        var studentId = "studentId";

        var questionnaireDto = new QuestionnaireDto();

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        //Agt & Assert
        assertThrows(StudentNotFoundException.class, () -> {
            questionnaireService.createQuestionnaire(questionnaireDto, studentId);
        });
    }

    @Test
    void updateQuestionnaire() {
        //Arrange
        var studentId = "studentId";

        var questionnaireDto = new QuestionnaireDto();
        questionnaireDto.setExperience("updatedExperience");

        var questionnaireEntity = new QuestionnaireEntity();
        questionnaireEntity.setId(1L);
        questionnaireEntity.setExperience("experience");

        var studentProjectWorkshop = new StudentProjectWorkshopEntity();
        studentProjectWorkshop.setQuestionnaire(questionnaireEntity);

        var student = new StudentEntity(
                studentId,
                "student",
                "student@mail",
                List.of(studentProjectWorkshop)
        );

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));



        var updatedQuestionnaireEntity = new QuestionnaireEntity();
        questionnaireEntity.setId(1L);
        questionnaireEntity.setExperience("updatedExperience");

        when(questionnaireRepository.findByStudentProjectWorkshop(studentProjectWorkshop))
                .thenReturn(Optional.of(questionnaireEntity));

        when(questionnaireMapper.toQuestionnaireEntity(questionnaireDto, studentProjectWorkshop, questionnaireEntity.getId()))
                .thenReturn(updatedQuestionnaireEntity);

        when(questionnaireRepository.save(updatedQuestionnaireEntity)).thenReturn(updatedQuestionnaireEntity);

        //Act
        questionnaireService.updateQuestionnaire(questionnaireDto, studentId);

        //Assert
        verify(questionnaireRepository).save(updatedQuestionnaireEntity);
        assertEquals(updatedQuestionnaireEntity, studentProjectWorkshop.getQuestionnaire());
    }

    @Test
    void updateQuestionnaireWithStudentNotFoundException() {
        //Arrange
        var studentId = "studentId";

        var questionnaireDto = new QuestionnaireDto();

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        //Agt & Assert
        assertThrows(StudentNotFoundException.class, () -> {
            questionnaireService.updateQuestionnaire(questionnaireDto, studentId);
        });
    }

    @Test
    void updateQuestionnaireWithQuestionnaireNotFoundException() {
        //Arrange
        var studentId = "studentId";

        var studentEntity = new StudentEntity();

        var questionnaireDto = new QuestionnaireDto();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentEntity));

        studentEntity.setStudentProjectWorkshop(List.of(new StudentProjectWorkshopEntity()));

        //Agt & Assert
        assertThrows(QuestionnaireNotFoundException.class, () -> {
            questionnaireService.updateQuestionnaire(questionnaireDto, studentId);
        });
    }
}