package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.mapper.QuestionnaireMapper;
import org.example.model.dto.QuestionnaireDto;
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
    var questionnaire =
        questionnaireRepository.findByStudent(studentRepository.findById(studentId).get());

    if (questionnaire.isPresent()) {
      return questionnaireMapper.questionnaireEntityToQuestionnaireDto(questionnaire.get());
    } else {
      throw new NullPointerException();
    }
  }

  @Override
  public void createQuestionnaire(QuestionnaireDto questionnaireDto, String studentId) {
    var student = studentRepository.findById(studentId);
    if (student.isPresent()) {
      questionnaireRepository.save(
          questionnaireMapper.questionnaireDtoToQuestionnaireEntity(
              questionnaireDto, student.get(), 0L));
    } else {
      throw new NullPointerException();
    }
  }

  @Override
  public void updateQuestionnaire(QuestionnaireDto questionnaireDto, String studentId) {
    var student = studentRepository.findById(studentId);
    var questionnaire = questionnaireRepository.findByStudent(student.get());

    if (student.isPresent() && questionnaire.isPresent()) {
      questionnaireRepository.save(
          questionnaireMapper.questionnaireDtoToQuestionnaireEntity(
              questionnaireDto, student.get(), questionnaire.get().getId()));
    } else {
      throw new NullPointerException();
    }
  }

  @Override
  public void deleteQuestionnaire(String studentId) {
    questionnaireRepository.delete(
        questionnaireRepository.findByStudent(studentRepository.findById(studentId).get()).get());
  }
}
