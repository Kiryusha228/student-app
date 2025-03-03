package org.example.repository;

import org.example.model.entity.HrEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HrRepository extends JpaRepository<HrEntity, Long> {
    @Override
    Optional<HrEntity> findById(Long telegramChatId);
}
