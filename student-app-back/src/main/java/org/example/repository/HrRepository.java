package org.example.repository;

import java.util.Optional;
import org.example.model.entity.HrEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HrRepository extends JpaRepository<HrEntity, Long> {
  @Override
  Optional<HrEntity> findById(Long telegramChatId);
}
