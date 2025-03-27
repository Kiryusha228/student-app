package org.example.repository;

import java.util.Optional;
import org.example.model.entity.StudentProjectWorkshopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentProjectWorkshopRepository
    extends JpaRepository<StudentProjectWorkshopEntity, Long> {
  @Override
  Optional<StudentProjectWorkshopEntity> findById(Long id);
}
