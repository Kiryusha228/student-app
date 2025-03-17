package org.example.mapper;

import org.example.model.dto.database.StudentTestResultDto;
import org.example.model.entity.StudentEntity;
import org.example.model.entity.StudentTestResultEntity;
import org.springframework.stereotype.Component;

@Component
public class StudentTestResultMapper {
    public StudentTestResultEntity testDtoToTestEntity(StudentTestResultDto testDto, StudentEntity studentEntity, Long testId) {
        return new StudentTestResultEntity(
                testId,
                testDto.getTestResult(),
                studentEntity
        );
    }

    public StudentTestResultDto testEntityToTestDto(StudentTestResultEntity testEntity) {
        return new StudentTestResultDto(
                testEntity.getTestResult()
        );
    }
}
