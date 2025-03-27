package org.example.repository;

import java.util.Optional;
import org.example.model.entity.QuestionnaireEntity;
import org.example.model.entity.StudentProjectWorkshopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionnaireRepository extends JpaRepository<QuestionnaireEntity, Long> {

  @Override
  Optional<QuestionnaireEntity> findById(Long id);

  Optional<QuestionnaireEntity> findByStudentProjectWorkshop(StudentProjectWorkshopEntity studentProjectWorkshop);
}
