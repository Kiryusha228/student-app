package org.example.repository;

import org.example.model.entity.StudentEntity;
import org.example.model.entity.StudentTestResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentTestResultRepository extends JpaRepository<StudentTestResultEntity, Long> {

    @Override
    Optional<StudentTestResultEntity> findById(Long id);

    Optional<StudentTestResultEntity> findByStudent(StudentEntity student);
}
