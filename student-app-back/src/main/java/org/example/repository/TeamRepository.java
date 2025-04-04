package org.example.repository;

import java.util.Optional;
import org.example.model.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
  @Override
  Optional<TeamEntity> findById(Long id);
}
