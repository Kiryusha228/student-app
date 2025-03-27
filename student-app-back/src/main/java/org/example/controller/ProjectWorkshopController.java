package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.database.CreateProjectWorkshopDto;
import org.example.service.ProjectWorkshopService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/project-workshop")
public class ProjectWorkshopController {
  private final ProjectWorkshopService projectWorkshopService;

  @PostMapping("/add")
  public void addProjectWorkshop(@RequestBody CreateProjectWorkshopDto createProjectWorkshopDto) {
    projectWorkshopService.createProjectWorkshop(createProjectWorkshopDto);
  }
}
