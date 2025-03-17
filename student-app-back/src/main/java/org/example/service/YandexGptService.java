package org.example.service;

import org.example.model.dto.database.StudentInfoDto;
import org.example.model.dto.yandexgpt.response.YandexGptResponse;

import java.util.List;

public interface YandexGptService {

    YandexGptResponse getTeams(List<StudentInfoDto> students);
}
