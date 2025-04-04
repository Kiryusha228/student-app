package org.example.repository;

import java.util.Optional;
import org.example.model.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestEntity, Long> {

  @Override
  Optional<TestEntity> findById(Long id);
}
