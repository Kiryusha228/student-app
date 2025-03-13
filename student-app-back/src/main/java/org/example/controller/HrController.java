package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.HrService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hr")
public class HrController {

  private final HrService hrService;

  @PostMapping("/add")
  public void addHr(Long telegramChatId) {
    hrService.createHr(telegramChatId);
  }

  @DeleteMapping("/delete")
  public void deleteHr(Long telegramChatId) {
    hrService.deleteHr(telegramChatId);
  }
}
