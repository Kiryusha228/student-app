package org.example.repository;

import org.example.model.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestRepository extends JpaRepository<TestEntity, Long> {

    @Override
    Optional<TestEntity> findById(Long id);
}
