package org.example.controller;

import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.database.StudentInTeamDto;
import org.example.model.dto.database.StudentInfoDto;
import org.example.service.StudentProjectWorkshopService;
import org.springframework.web.bind.annotation.GetMapping;
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

  @PostMapping("/info") // todo разобраться почему он хочет post
  public List<StudentInfoDto> getAllStudentInfo() {
    return studentProjectWorkshopService.getAllPastStudents();
  }

  @GetMapping("/team")
  public List<StudentInTeamDto> getTeam(Principal principal) {
    return studentProjectWorkshopService.getTeam(principal.getName());
  }

  @GetMapping("/get-all-past-students")
  public List<StudentInfoDto> getAllPastStudents() {
    return studentProjectWorkshopService.getAllPastStudents();
  }

  @GetMapping("/get-all-students")
  public List<StudentInfoDto> getAllStudentsByProjectWorkshopId(Long projectWorkshopId) {
    return studentProjectWorkshopService.getAllStudentsByProjectWorkshopId(projectWorkshopId);
  }
}
