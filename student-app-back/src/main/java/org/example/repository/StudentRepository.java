package org.example.repository;

import org.example.model.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentEntity, String> {

    @Override
    Optional<StudentEntity> findById(String id);
}
