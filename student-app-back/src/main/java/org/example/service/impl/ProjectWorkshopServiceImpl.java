package org.example.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.exception.ProjectWorkshopNotFoundException;
import org.example.mapper.ProjectWorkshopMapper;
import org.example.model.dto.database.CreateProjectWorkshopDto;
import org.example.model.entity.StudentProjectWorkshopEntity;
import org.example.repository.ProjectWorkshopRepository;
import org.example.repository.StudentProjectWorkshopRepository;
import org.example.service.ProjectWorkshopService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectWorkshopServiceImpl implements ProjectWorkshopService {
  private final StudentProjectWorkshopRepository studentProjectWorkshopRepository;
  private final ProjectWorkshopRepository projectWorkshopRepository;
  private final ProjectWorkshopMapper projectWorkshopMapper;

  @Override
  public void createProjectWorkshop(CreateProjectWorkshopDto createProjectWorkshopDto) {
    projectWorkshopRepository.save(
        projectWorkshopMapper.toProjectWorkshopEntity(createProjectWorkshopDto));
  }

  @Override
  @Transactional
  public void addStudent(
      Long projectWorkshopId, StudentProjectWorkshopEntity studentProjectWorkshop) {
    var projectWorkshopOptional = projectWorkshopRepository.findById(projectWorkshopId);

    if (projectWorkshopOptional.isEmpty()) {
      throw new ProjectWorkshopNotFoundException("Такой мастерской не существует");
    }
    var projectWorkshop = projectWorkshopOptional.get();

    projectWorkshop.getStudentProjectWorkshop().add(studentProjectWorkshop);
    studentProjectWorkshop.setProjectWorkshop(projectWorkshop);
  }

  @Override
  public Long getLastProjectWorkshopId() {
    // todo: возможно сделать лучше
    var projectWorkshops = projectWorkshopRepository.findAll();
    return projectWorkshops.get(projectWorkshops.size() - 1).getId();
  }
}
