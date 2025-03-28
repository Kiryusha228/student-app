package org.example.repository;

import org.example.model.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
    @Override
    Optional<TeamEntity> findById(Long id);
}
