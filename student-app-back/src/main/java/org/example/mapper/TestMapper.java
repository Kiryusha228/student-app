package org.example.mapper;

import org.example.model.dto.TestDto;
import org.example.model.entity.StudentEntity;
import org.example.model.entity.TestEntity;

public class TestMapper {
    static TestEntity testDtoToTestEntity(TestDto testDto, StudentEntity studentEntity){
        return new TestEntity(
                testDto.getId(),
                testDto.getTestResult(),
                testDto.getContestResult(),
                studentEntity
        );
    }

    static TestDto testEntityToTestDto(TestEntity testEntity){
        return new TestDto(
                testEntity.getId(),
                testEntity.getTestResult(),
                testEntity.getContestResult(),
                testEntity.getStudent().getId());
    }
}
