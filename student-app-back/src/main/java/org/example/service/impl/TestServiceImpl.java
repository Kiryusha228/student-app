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
    public TestDto getTestById(Long testId) {
        var test = testRepository.findById(testId);
        if (test.isPresent()){
            return testMapper.testEntityToTestDto(test.get());
        }
        else {
            throw new NullPointerException();
        }
    }

    @Override
    public void createTest(TestDto testDto) {
        var student = studentRepository.findById(testDto.getStudentId());
        if (student.isPresent()){
            testRepository.save(testMapper.testDtoToTestEntity(testDto, student.get()));
        }
        else {
            throw new NullPointerException();
        }
    }

    @Override
    public void deleteTest(Long testId) {
        testRepository.deleteById(testId);
    }
}
