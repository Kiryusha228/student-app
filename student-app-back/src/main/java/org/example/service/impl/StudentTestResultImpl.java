package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.mapper.TestMapper;
import org.example.model.dto.StudentTestResultDto;
import org.example.repository.StudentRepository;
import org.example.repository.StudentTestResultRepository;
import org.example.service.StudentTestResultService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StudentTestResultImpl implements StudentTestResultService {

    private final StudentTestResultRepository testRepository;
    private final StudentRepository studentRepository;
    private final TestMapper testMapper;

    @Override
    public StudentTestResultDto getTestResult(String studentId) {
        var test = testRepository.findByStudent(
                studentRepository.findById(studentId).get()
        );

        if (test.isPresent()){
            return testMapper.testEntityToTestDto(test.get());
        }
        else {
            throw new NullPointerException();
        }
    }

    @Override
    public void createTestResult(StudentTestResultDto testDto, String studentId) {
        var student = studentRepository.findById(studentId);

        if (student.isPresent()){
            testRepository.save(
                    testMapper.testDtoToTestEntity(
                            testDto,
                            student.get(),
                            0L)
            );
        }
        else {
            throw new NullPointerException();
        }
    }

    @Override
    public void deleteTestResult(String studentId) {
        testRepository.delete(
                testRepository.findByStudent(
                        studentRepository.findById(studentId).get()
                ).get()
        );
    }
}
