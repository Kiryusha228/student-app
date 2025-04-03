package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.YandexGptService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gpt")
public class YandexGptController {
  private final YandexGptService yandexGptService;

  @PostMapping("/teams")
  public void getTeams(String teamCount) {
    yandexGptService.getTeams(teamCount);
  }
}
