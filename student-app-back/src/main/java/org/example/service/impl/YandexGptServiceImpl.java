package org.example.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.exception.ProjectWorkshopEnabledException;
import org.example.exception.YandexGptResponseNotFoundException;
import org.example.model.dto.database.StudentInfoDto;
import org.example.model.dto.database.TeamListDto;
import org.example.model.dto.yandexgpt.Message;
import org.example.model.dto.yandexgpt.request.CompletionOptions;
import org.example.model.dto.yandexgpt.request.YandexGptRequest;
import org.example.model.dto.yandexgpt.response.YandexGptResponse;
import org.example.service.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class YandexGptServiceImpl implements YandexGptService {

  private final WebClient webClient;

  private final Dotenv dotenv;

  private final ObjectMapper objectMapper;

  private final TeamService teamService;
  private final StudentProjectWorkshopService studentProjectWorkshopService;
  private final ProjectWorkshopService projectWorkshopService;
  private final FileReaderService fileReaderService;

  @Override
  @Transactional
  public void getTeams(String teamCount) {
    if (projectWorkshopService.getLastProjectWorkshop().getIsEnable()) {
      throw new ProjectWorkshopEnabledException("Набор на мастерскую еще открыт");
    }

    var students = studentProjectWorkshopService.getAllPastStudents();

    var apiUrl = dotenv.get("YANDEX_API_URL");
    var apiKey = dotenv.get("YANDEX_API_KEY");
    var folderId = dotenv.get("YANDEX_FOLDER_ID");

    YandexGptRequest request = new YandexGptRequest();
    try {
      request = getYandexGptTeamRequest(folderId, students, teamCount);
    } catch (JsonProcessingException ex) {

      // todo: add logger
      ex.printStackTrace();
    }

    Mono<YandexGptResponse> responseMono =
        webClient
            .post()
            .uri(apiUrl)
            .header(HttpHeaders.AUTHORIZATION, "Api-Key " + apiKey)
            .header("x-folder-id", folderId)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(YandexGptResponse.class);

    var createdTeams = responseMono.block();

    if (createdTeams == null) {
      throw new YandexGptResponseNotFoundException(
          "Произошла ошибка при получении ответа от YandexGpt");
    }

    var resultStringJson =
        createdTeams
            .getResult()
            .getAlternatives()
            .get(0)
            .getMessage()
            .getText()
            .replaceAll("`", "");

    var teams = new TeamListDto();
    try {
      teams = objectMapper.reader().readValue(resultStringJson, TeamListDto.class);
    } catch (IOException exception) {
      exception.printStackTrace();
    }

    teamService.createTeams(teams);
  }

  private YandexGptRequest getYandexGptTeamRequest(
      String folderId, List<StudentInfoDto> students, String teamCount)
      throws JsonProcessingException {
    var completionOptions = new CompletionOptions(false, 0.6, 10000);

    //    var command =
    //
    // fileReaderService.getFromFile("student-app-back/src/main/resources/yandexGptCommand.txt");
    var command = fileReaderService.getFromFile("app/resources/yandexGptCommand.txt"); // для докера
    command = command.replace("'teamsCount'", teamCount);

    var systemMessage = new Message("system", command);

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String json = ow.writeValueAsString(students);

    var userMessage = new Message("user", json);

    return new YandexGptRequest(
        "gpt://" + folderId + "/yandexgpt/latest",
        completionOptions,
        List.of(systemMessage, userMessage));
  }
}
