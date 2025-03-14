package org.example.service;

import org.example.model.dto.StudentInfoDto;
import org.example.model.dto.yandexgpt.response.YandexGptResponse;

import java.util.List;

public interface YandexGptService {
    YandexGptResponse apiTest();

    YandexGptResponse getTeams(List<StudentInfoDto> students);
}
