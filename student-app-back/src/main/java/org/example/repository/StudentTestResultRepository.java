package org.example.repository;

import java.util.Optional;
import org.example.model.entity.StudentProjectWorkshopEntity;
import org.example.model.entity.StudentTestResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentTestResultRepository extends JpaRepository<StudentTestResultEntity, Long> {

  @Override
  Optional<StudentTestResultEntity> findById(Long id);

  Optional<StudentTestResultEntity> findByStudentProjectWorkshop(
      StudentProjectWorkshopEntity studentProjectWorkshop);
}
