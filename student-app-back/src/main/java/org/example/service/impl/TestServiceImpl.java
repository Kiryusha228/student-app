package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.mapper.TestMapper;
import org.example.model.dto.TestDto;
import org.example.repository.StudentRepository;
import org.example.repository.TestRepository;
import org.example.service.TestService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final StudentRepository studentRepository;
    private final TestMapper testMapper;

    @Override
    public TestDto getTest(String studentId) {
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
    public void createTest(TestDto testDto, String studentId) {
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
    public void deleteTest(String studentId) {
        testRepository.delete(
                testRepository.findByStudent(
                        studentRepository.findById(studentId).get()
                ).get()
        );
    }
}
