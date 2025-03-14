package org.example.service.impl;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.example.mapper.QuestionnaireMapper;
import org.example.mapper.StudentMapper;
import org.example.mapper.StudentTestResultMapper;
import org.example.model.dto.StudentInfoDto;
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
    public List<StudentInfoDto> getAllStudents() {
        var students = studentRepository.findAll().stream().toList();

        var allStudentsInfo = new ArrayList<StudentInfoDto>();

        for (StudentEntity student : students) {
            var testResult = studentTestResultRepository.findByStudent(student);
            var questionnaire = questionnaireRepository.findByStudent(student);
            if (testResult.isPresent() && questionnaire.isPresent()) {
                allStudentsInfo.add(
                        studentMapper.toStudentInfoDto(
                                student,
                                questionnaire.get(),
                                testResult.get()));
            }
        }

        return allStudentsInfo;
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
