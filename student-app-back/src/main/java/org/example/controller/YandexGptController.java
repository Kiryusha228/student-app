package org.example.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.database.StudentInfoDto;
import org.example.model.dto.yandexgpt.response.YandexGptResponse;
import org.example.service.YandexGptService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gpt")
public class YandexGptController {
  private final YandexGptService yandexGptService;

  @PostMapping("/teams")
  public YandexGptResponse getTeams(@RequestBody List<StudentInfoDto> students) {
    return yandexGptService.getTeams(students);
  }
}
