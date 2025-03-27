package org.example.repository;

import java.util.Optional;
import org.example.model.entity.ProjectWorkshopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectWorkshopRepository extends JpaRepository<ProjectWorkshopEntity, Long> {
  @Override
  Optional<ProjectWorkshopEntity> findById(Long id);
}
