package org.example.mapper;


import org.example.model.dto.StudentDto;
import org.example.model.entity.StudentEntity;

public class UserMapper {
    StudentDto studentEntityToStudentDto(StudentEntity studentEntity){
        return new StudentDto();
    }
}
