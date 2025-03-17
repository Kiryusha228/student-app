package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.mapper.HrMapper;
import org.example.repository.HrRepository;
import org.example.service.HrService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HrServiceImpl implements HrService {

  private final HrRepository hrRepository;
  private final HrMapper hrMapper;

  @Override
  public void createHr(Long telegramChatId) {
    hrRepository.save(hrMapper.telegramChatIdToHrEntity(telegramChatId));
  }

  @Override
  public void deleteHr(Long telegramChatId) {
    hrRepository.deleteById(telegramChatId);
  }
}
