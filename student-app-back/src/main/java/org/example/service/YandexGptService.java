package org.example.service;

import java.util.List;
import org.example.model.dto.database.StudentInfoDto;
import org.example.model.dto.database.TeamListDto;

public interface YandexGptService {

  TeamListDto getTeams(List<StudentInfoDto> students);

}
