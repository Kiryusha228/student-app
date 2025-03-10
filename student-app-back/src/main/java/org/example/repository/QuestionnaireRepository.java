package org.example.repository;

import org.example.model.entity.QuestionnaireEntity;
import org.example.model.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionnaireRepository extends JpaRepository<QuestionnaireEntity, Long> {

    @Override
    Optional<QuestionnaireEntity> findById(Long id);

    Optional<QuestionnaireEntity> findByStudent(StudentEntity student);
}
