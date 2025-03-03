package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.mapper.QuestionnaireMapper;
import org.example.model.dto.QuestionnaireDto;
import org.example.model.entity.QuestionnaireEntity;
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
    public QuestionnaireDto getQuestionnaireById(Long questionnaireId) {
        var questionnaire = questionnaireRepository.findById(questionnaireId);

        if (questionnaire.isPresent()) {
            return questionnaireMapper.questionnaireEntityToQuestionnaireDto(questionnaire.get());
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    public void createQuestionnaire(QuestionnaireDto questionnaireDto) {
        var student = studentRepository.findById(questionnaireDto.getStudentId());
        if (student.isPresent()) {
            questionnaireRepository.save(questionnaireMapper.questionnaireDtoToQuestionnaireEntity(questionnaireDto, student.get()));
        }
        else {
            throw new NullPointerException();
        }
    }

    @Override
    public void updateQuestionnaire(QuestionnaireDto questionnaireDto) {
        var student = studentRepository.findById(questionnaireDto.getStudentId());
        var questionnaire = questionnaireRepository.findById(questionnaireDto.getId());
        if (student.isPresent() && questionnaire.isPresent()) {
            questionnaireRepository.save(questionnaireMapper.questionnaireDtoToQuestionnaireEntity(questionnaireDto, student.get()));
        }
        else {
            throw new NullPointerException();
        }
    }

    @Override
    public void deleteQuestionnaire(Long questionnaireId) {
        questionnaireRepository.deleteById(questionnaireId);
    }
}
