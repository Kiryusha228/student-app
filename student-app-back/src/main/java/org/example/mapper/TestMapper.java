package org.example.mapper;

import org.example.model.dto.TestDto;
import org.example.model.entity.StudentEntity;
import org.example.model.entity.TestEntity;
import org.springframework.stereotype.Component;

@Component
public class TestMapper {
    public TestEntity testDtoToTestEntity(TestDto testDto, StudentEntity studentEntity, Long testId) {
        return new TestEntity(
                testId,
                testDto.getTestResult(),
                studentEntity
        );
    }

    public TestDto testEntityToTestDto(TestEntity testEntity) {
        return new TestDto(
                testEntity.getTestResult()
        );
    }
}
