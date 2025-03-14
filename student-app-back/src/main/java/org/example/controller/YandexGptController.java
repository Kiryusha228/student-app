package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.StudentInfoDto;
import org.example.model.dto.yandexgpt.response.YandexGptResponse;
import org.example.service.YandexGptService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gpt")
public class YandexGptController {
    private final YandexGptService yandexGptService;

    @GetMapping("/test")
    public YandexGptResponse apiTest() {
        return yandexGptService.apiTest();
    }

    @PostMapping("/teams")
    public YandexGptResponse getTeams(@RequestBody List<StudentInfoDto> students) {
        return yandexGptService.getTeams(students);
    }

}
