package org.example.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.exception.YandexGptResponseNotFoundException;
import org.example.model.dto.database.StudentInfoDto;
import org.example.model.dto.database.TeamListDto;
import org.example.model.dto.yandexgpt.Message;
import org.example.model.dto.yandexgpt.request.CompletionOptions;
import org.example.model.dto.yandexgpt.request.YandexGptRequest;
import org.example.model.dto.yandexgpt.response.YandexGptResponse;
import org.example.service.StudentProjectWorkshopService;
import org.example.service.TeamService;
import org.example.service.YandexGptService;
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

//  private static final String gptCommand =
//      "Ты — помощник, который решает задачи по распределению студентов на команды. Тебе будет предоставлен JSON со списком студентов, где для каждого студента указаны следующие параметры:\n"
//          + "- `studentProjectWorkshopId`: Id студента.\n"
//          + "- `testResult`: Числовое значение результата теста (от 0 до 100).\n"
//          + "- `experience`: Описание опыта работы или обучения.\n"
//          + "- `languageProficiency`: Уровень владения языками программирования.\n"
//          + "- `languageExperience`: Конкретный опыт использования технологий.\n"
//          + "- `telegram`: Telegram-ник для связи.\n"
//          + "- `role`: Предпочтительная роль (`frontend`, `backend`, `qa`, `all`).\n"
//          + "\n"
//          + "Задача: Распределить всех студентов на ровно 2 команды таким образом, чтобы:\n"
//          + "1. Средний уровень знаний был примерно одинаковым во всех командах. Для расчета уровня знаний используй следующие параметры:\n"
//          + "   - `testResult`: Числовое значение результата теста.\n"
//          + "   - `experience`: Описание опыта работы или обучения. Например, студенты с большим опытом (`2+ года`) или значительным описанием навыков должны быть распределены более равномерно.\n"
//          + "   - `languageProficiency`: Уровень владения языками программирования (например, продвинутый уровень Python или JavaScript может повысить общий уровень студента).\n"
//          + "   - `languageExperience`: Конкретный опыт использования технологий (например, опыт работы с фреймворками React, Django или Selenium может считаться плюсом).\n"
//          + "2. В каждой команде должна быть хотя бы одна роль из каждой категории (`frontend`, `backend`, `qa`). Если студент имеет роль `all`, он может занимать любую из этих ролей. Если невозможно соблюсти строгое распределение ролей, максимизируй разнообразие ролей в командах.\n"
//          + "3. В командах должно быть одинаковое количество участников, если это невозможно, то разница в количестве участников должна быть минимальной"
//          + "4. Все студенты должны быть распределены по командам без исключения. В ответе должно быть столько же студентов, сколько и во входных данных, и каждый студент должен встречаться только один раз.\n"
//          + "\n"
//          + "Ответ должен быть представлен в виде JSON следующего формата:\n"
//          + "{\n"
//          + "  \"teams\": [\n"
//          + "    {\n"
//          + "      \"students\": [ id студента 1, id студента 2, … ]\n"
//          + "    },\n"
//          + "    {\n"
//          + "      \"students\": [ id студента 1, id студента 2, … ]\n"
//          + "      ]\n"
//          + "    },\n"
//          + "    ...\n"
//          + "  ]\n"
//          + "}"
//          + "\n"
//          + "Нужны только данные без вводных фраз и объяснений. Не используй разметку Markdown!";

  @Override
  public TeamListDto getTeams(List<StudentInfoDto> students1) {

    var students = studentProjectWorkshopService.getAllStudents();

    var apiUrl = dotenv.get("YANDEX_API_URL");
    var apiKey = dotenv.get("YANDEX_API_KEY");
    var folderId = dotenv.get("YANDEX_FOLDER_ID");

    YandexGptRequest request = new YandexGptRequest();
    try {
      request = getYandexGptTeamRequest(folderId, students);
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
      throw new YandexGptResponseNotFoundException("Произошла ошибка при получении ответа от YandexGpt");
    }

    var resultStringJson = createdTeams
            .getResult()
            .getAlternatives()
            .get(0)
            .getMessage()
            .getText()
            .replaceAll("`", "");

    System.out.println(resultStringJson);

    var teams = new TeamListDto();
    try {
      teams = objectMapper.reader().readValue(resultStringJson, TeamListDto.class);
    }
    catch (IOException exception) {
      exception.printStackTrace();
    }

    teamService.createTeams(teams);

    return teams;
  }

  private static YandexGptRequest getYandexGptTeamRequest(
      String folderId, List<StudentInfoDto> students) throws JsonProcessingException {
    var completionOptions = new CompletionOptions(false, 0.6, 10000);

    var gptCommand = "";
    try {
      gptCommand = Files.readString(Path.of("student-app-back/src/main/resources/yandexGptCommand.txt"));
    }
    catch (IOException exception) {
      exception.printStackTrace();
    }

    var systemMessage = new Message("system", gptCommand);

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String json = ow.writeValueAsString(students);

    var userMessage = new Message("user", json);

    return new YandexGptRequest(
        "gpt://" + folderId + "/yandexgpt/latest",
        completionOptions,
        List.of(systemMessage, userMessage));
  }
}
