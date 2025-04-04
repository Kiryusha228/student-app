package org.example.service.impl;

import jakarta.transaction.Transactional;
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
  private final StudentRepository studentRepository;
  private final QuestionnaireRepository questionnaireRepository;
  private final QuestionnaireMapper questionnaireMapper;

  @Override
  public QuestionnaireDto getQuestionnaireByStudentId(String studentId) {
    var student = studentRepository.findById(studentId);

    if (student.isEmpty()) {
      throw new StudentNotFoundException("Студент не найден");
    }

    var studentProjectWorkshops = student.get().getStudentProjectWorkshop();
    var lastStudentProjectWorkshop =
        studentProjectWorkshops.get(studentProjectWorkshops.size() - 1);
    var questionnaire = lastStudentProjectWorkshop.getQuestionnaire();

    if (questionnaire == null) {
      throw new QuestionnaireNotFoundException("У студента не заполнена анекта");
    }

    return questionnaireMapper.toQuestionnaireDto(questionnaire);
  }

  @Override
  @Transactional
  public void createQuestionnaire(QuestionnaireDto questionnaireDto, String studentId) {
    var student = studentRepository.findById(studentId);

    if (student.isEmpty()) {
      throw new StudentNotFoundException("Студент не найден");
    }

    var studentProjectWorkshops = student.get().getStudentProjectWorkshop();
    var lastStudentProjectWorkshop =
        studentProjectWorkshops.get(studentProjectWorkshops.size() - 1);

    var savedQuestionnaire =
        questionnaireRepository.save(
            questionnaireMapper.toQuestionnaireEntity(
                questionnaireDto, lastStudentProjectWorkshop, 0L));

    lastStudentProjectWorkshop.setQuestionnaire(savedQuestionnaire);
  }

  @Override
  @Transactional
  public void updateQuestionnaire(QuestionnaireDto questionnaireDto, String studentId) {
    var student = studentRepository.findById(studentId);

    if (student.isEmpty()) {
      throw new StudentNotFoundException("Студент не найден");
    }

    var studentProjectWorkshops = student.get().getStudentProjectWorkshop();
    var lastStudentProjectWorkshop =
        studentProjectWorkshops.get(studentProjectWorkshops.size() - 1);

    var questionnaire =
        questionnaireRepository.findByStudentProjectWorkshop(lastStudentProjectWorkshop);

    if (questionnaire.isEmpty()) {
      throw new QuestionnaireNotFoundException("У студента не заполнена анекта");
    }

    var savedQuestionnaire =
        questionnaireRepository.save(
            questionnaireMapper.toQuestionnaireEntity(
                questionnaireDto,
                lastStudentProjectWorkshop,
                lastStudentProjectWorkshop.getQuestionnaire().getId()));

    lastStudentProjectWorkshop.setQuestionnaire(savedQuestionnaire);
  }
}
