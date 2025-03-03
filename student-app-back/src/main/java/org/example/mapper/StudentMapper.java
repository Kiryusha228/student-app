package org.example.mapper;


import org.example.model.dto.StudentDto;
import org.example.model.entity.StudentEntity;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    public StudentDto studentEntityToStudentDto(StudentEntity studentEntity){
        return new StudentDto();
    }
}
