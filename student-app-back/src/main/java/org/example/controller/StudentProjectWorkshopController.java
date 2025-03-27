package org.example.controller;

import java.security.Principal;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.database.StudentInfoDto;
import org.example.service.StudentProjectWorkshopService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student-project-workshop")
public class StudentProjectWorkshopController {
  private final StudentProjectWorkshopService studentProjectWorkshopService;

  @PostMapping("/add")
  public void addStudentProjectWorkshop(Principal principal) {
    studentProjectWorkshopService.createStudentProjectWorkshop(principal.getName());
  }

  @PostMapping("/info")
  public List<StudentInfoDto> getAllStudentInfo() {
    return studentProjectWorkshopService.getAllStudents();
  }
}
