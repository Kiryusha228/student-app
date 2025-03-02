package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.entity.StudentEntity;
import org.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class StudentController {

    @Autowired
    private final StudentService studentService;

    @PostMapping("/add")
    public void addStudent(@RequestBody StudentEntity studentEntity) {
        studentService.createStudent(studentEntity);
    }

    @PatchMapping("/update")
    public void updateStudent(@RequestBody StudentEntity newStudentEntity) {
        studentService.updateStudent(newStudentEntity);
    }

    @DeleteMapping("/delete")
    public void deleteStudent(Long studentId) {
        studentService.deleteStudent(studentId);
    }

    @GetMapping
    public List<StudentEntity> getAllStudents() {
        return studentService.getAllStudents();
    }

}
