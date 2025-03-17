package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.exception.QuestionnaireNotFoundException;
import org.example.exception.StudentNotFoundException;
import org.example.mapper.QuestionnaireMapper;
import org.example.model.dto.database.QuestionnaireDto;
import org.example.repository.QuestionnaireRepository;
import org.example.repository.StudentRepository;
import org.example.service.QuestionnaireService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionnaireServiceImpl implements QuestionnaireService {

    private final QuestionnaireRepository questionnaireRepository;
    private final StudentRepository studentRepository;
    private final QuestionnaireMapper questionnaireMapper;

    @Override
    public QuestionnaireDto getQuestionnaireByStudentId(String studentId) {
        var student = studentRepository.findById(studentId);

        if (student.isEmpty()) {
            throw new StudentNotFoundException("Студент не найден");
        }

        var questionnaire = questionnaireRepository.findByStudent(student.get());

        if (questionnaire.isEmpty()) {
            throw new QuestionnaireNotFoundException("У данного студента не заполнена анкета");
        }

        return questionnaireMapper.questionnaireEntityToQuestionnaireDto(questionnaire.get());
    }

    @Override
    public void createQuestionnaire(QuestionnaireDto questionnaireDto, String studentId) {
        var student = studentRepository.findById(studentId);

        if (student.isEmpty()) {
            throw new StudentNotFoundException("Студент не найден");
        }

        questionnaireRepository.save(
                questionnaireMapper.questionnaireDtoToQuestionnaireEntity(
                        questionnaireDto, student.get(), 0L));
    }

    @Override
    public void updateQuestionnaire(QuestionnaireDto questionnaireDto, String studentId) {
        var student = studentRepository.findById(studentId);

        if (student.isEmpty()) {
            throw new StudentNotFoundException("Студент не найден");
        }

        var questionnaire = questionnaireRepository.findByStudent(student.get());

        if (questionnaire.isEmpty()) {
            throw new QuestionnaireNotFoundException("У данного студента не заполнена анкета");
        }

        questionnaireRepository.save(
                questionnaireMapper.questionnaireDtoToQuestionnaireEntity(
                        questionnaireDto, student.get(), questionnaire.get().getId()));
    }

    @Override
    public void deleteQuestionnaire(String studentId) {
        var student = studentRepository.findById(studentId);

        if (student.isEmpty()) {
            throw new StudentNotFoundException("Студент не найден");
        }

        var questionnaire = questionnaireRepository.findByStudent(student.get());

        if (questionnaire.isEmpty()) {
            throw new QuestionnaireNotFoundException("У данного студента не заполнена анкета");
        }

        questionnaireRepository.delete(questionnaire.get());
    }
}
