package org.example.mapper;

import org.example.model.dto.TestDto;
import org.example.model.entity.StudentEntity;
import org.example.model.entity.TestEntity;
import org.springframework.stereotype.Component;

@Component
public class TestMapper {
    public TestEntity testDtoToTestEntity(TestDto testDto, StudentEntity studentEntity){
        return new TestEntity(
                testDto.getId(),
                testDto.getTestResult(),
                testDto.getContestResult(),
                studentEntity
        );
    }

    public TestDto testEntityToTestDto(TestEntity testEntity){
        return new TestDto(
                testEntity.getId(),
                testEntity.getTestResult(),
                testEntity.getContestResult(),
                testEntity.getStudent().getId());
    }
}
