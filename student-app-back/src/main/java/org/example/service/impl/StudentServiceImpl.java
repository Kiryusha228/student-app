package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.entity.StudentEntity;
import org.example.repository.StudentRepository;
import org.example.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

//
//    public StudentServiceImpl(StudentRepository studentRepository) { //почему оно просит конструктор?
//        this.studentRepository = studentRepository;
//    }

    @Override
    public List<StudentEntity> getAllStudents() {
        return studentRepository.findAll().stream().toList();
    }

    @Override
    public StudentEntity getStudentById(Long studentId) {
        return studentRepository.findById(studentId).get();
    }

    @Override
    public void createStudent(StudentEntity studentEntity) {
        studentRepository.save(studentEntity);
    }

    @Override
    public void updateStudent(StudentEntity newStudentEntity) {
        studentRepository.save(newStudentEntity); //возможно надо менять
        //studentRepository.findById(studentEntity.getId) lombok видимо сдох
    }

    @Override
    public void deleteStudent(Long studentId) {
        studentRepository.deleteById(studentId);
    }
}
