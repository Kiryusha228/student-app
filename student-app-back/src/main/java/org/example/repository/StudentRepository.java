package org.example.repository;

import java.util.Optional;
import org.example.model.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity, String> {

  @Override
  Optional<StudentEntity> findById(String id);

  Optional<StudentEntity> findByMail(String mail);

  Optional<StudentEntity> findByName(String name);
}
