package org.example.controller;

import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.mapper.StudentMapper;
import org.example.model.dto.StudentDto;
import org.example.model.entity.StudentEntity;
import org.example.service.StudentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
public class StudentController {

  private final StudentService studentService;
  private final StudentMapper studentMapper;

  @PostMapping("/add")
  public void addStudent(Principal principal, @RequestBody StudentDto studentDto) {
    studentService.createStudent(
        studentMapper.studentDtoToStudentEntity(studentDto, principal.getName()));
  }

  @GetMapping("/get")
  public StudentEntity getStudent(Principal principal) {
    return studentService.getStudentById(principal.getName());
  }

  @PatchMapping("/update")
  public void updateStudent(Principal principal, @RequestBody StudentDto newStudentDto) {
    studentService.updateStudent(
        studentMapper.studentDtoToStudentEntity(newStudentDto, principal.getName()));
  }

  @DeleteMapping("/delete")
  public void deleteStudent(Principal principal) {
    studentService.deleteStudent(principal.getName());
  }

  @GetMapping
  public List<StudentEntity> getAllStudents() {
    return studentService.getAllStudents();
  }
}
