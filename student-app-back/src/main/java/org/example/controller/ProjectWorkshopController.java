package org.example.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.database.CreateProjectWorkshopDto;
import org.example.model.dto.database.ProjectWorkshopDto;
import org.example.model.dto.database.TeamWithStudentInfoDto;
import org.example.service.ProjectWorkshopService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/project-workshop")
public class ProjectWorkshopController {
  private final ProjectWorkshopService projectWorkshopService;

  @PostMapping("/add")
  public void addProjectWorkshop(@RequestBody CreateProjectWorkshopDto createProjectWorkshopDto) {
    projectWorkshopService.createProjectWorkshop(createProjectWorkshopDto);
  }

  @GetMapping("/team")
  public List<TeamWithStudentInfoDto> getTeam(Long projectWorkshopId) {
    return projectWorkshopService.getTeams(projectWorkshopId);
  }

  @GetMapping("/get/all")
  public List<ProjectWorkshopDto> getProjectWorkshops() {
    return projectWorkshopService.getProjectWorkshops();
  }

  @GetMapping("/get/id")
  public ProjectWorkshopDto getProjectWorkshopById(Long projectWorkshopId) {
    return projectWorkshopService.getProjectWorkshopById(projectWorkshopId);
  }

  @GetMapping("/get/last-id")
  public Long getLastProjectWorkshopId() {
    return projectWorkshopService.getLastProjectWorkshopId();
  }

  @GetMapping("/get/last")
  public ProjectWorkshopDto getLastProjectWorkshop() {
    return projectWorkshopService.getLastProjectWorkshop();
  }

  @PostMapping("/enable")
  public void enableProjectWorkshop(Long projectWorkshopId) {
    projectWorkshopService.enableProjectWorkshop(projectWorkshopId);
  }

  @PostMapping("/disable")
  public void disableProjectWorkshop(Long projectWorkshopId) {
    projectWorkshopService.disableProjectWorkshop(projectWorkshopId);
  }
}
