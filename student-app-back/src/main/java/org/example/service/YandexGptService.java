package org.example.service;

import java.util.List;
import org.example.model.dto.database.StudentInfoDto;
import org.example.model.dto.yandexgpt.response.YandexGptResponse;

public interface YandexGptService {

  YandexGptResponse getTeams(List<StudentInfoDto> students);
}
