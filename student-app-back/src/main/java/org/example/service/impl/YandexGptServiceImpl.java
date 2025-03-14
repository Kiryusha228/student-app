package org.example.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.cdimascio.dotenv.Dotenv;
import org.example.model.dto.StudentInfoDto;
import org.example.model.dto.yandexgpt.Message;
import org.example.model.dto.yandexgpt.request.CompletionOptions;
import org.example.model.dto.yandexgpt.response.YandexGptResponse;
import org.example.model.dto.yandexgpt.request.YandexGptRequest;
import org.example.service.YandexGptService;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import reactor.core.publisher.Mono;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class YandexGptServiceImpl implements YandexGptService {

    private static final Dotenv dotenv = Dotenv.configure().load();

    private static final String gptCommand = "Ты — помощник, который решает задачи по распределению студентов на команды. Тебе будет предоставлен JSON со списком студентов, где для каждого студента указаны следующие параметры:\n" +
            "- `studentName`: Имя студента.\n" +
            "- `testResult`: Числовое значение результата теста (от 0 до 100).\n" +
            "- `experience`: Описание опыта работы или обучения.\n" +
            "- `languageProficiency`: Уровень владения языками программирования.\n" +
            "- `languageExperience`: Конкретный опыт использования технологий.\n" +
            "- `telegram`: Telegram-ник для связи.\n" +
            "- `role`: Предпочтительная роль (`frontend`, `backend`, `qa`, `all`).\n" +
            "\n" +
            "Задача: Распределить всех студентов на ровно 2 команды таким образом, чтобы:\n" +
            "1. Средний уровень знаний был примерно одинаковым во всех командах. Для расчета уровня знаний используй следующие параметры:\n" +
            "   - `testResult`: Числовое значение результата теста.\n" +
            "   - `experience`: Описание опыта работы или обучения. Например, студенты с большим опытом (`2+ года`) или значительным описанием навыков должны быть распределены более равномерно.\n" +
            "   - `languageProficiency`: Уровень владения языками программирования (например, продвинутый уровень Python или JavaScript может повысить общий уровень студента).\n" +
            "   - `languageExperience`: Конкретный опыт использования технологий (например, опыт работы с фреймворками React, Django или Selenium может считаться плюсом).\n" +
            "2. В каждой команде должна быть хотя бы одна роль из каждой категории (`frontend`, `backend`, `qa`). Если студент имеет роль `all`, он может занимать любую из этих ролей. Если невозможно соблюсти строгое распределение ролей, максимизируй разнообразие ролей в командах.\n" +
            "3. В командах должно быть одинаковое количество участников, если это невозможно, то разница в количестве участников должна быть минимальной" +
            "4. Все студенты должны быть распределены по командам без исключения. В ответе должно быть столько же студентов, сколько и во входных данных, и каждый студент должен встречаться только один раз.\n" +
            "\n" +
            "Ответ должен быть представлен в виде JSON следующего формата:\n" +
            "\n" +
            "{\n" +
            "  \"teams\": [\n" +
            "    {\n" +
            "      \"teamName\": \"Команда 1\",\n" +
            "      \"members\": [\n" +
            "        {\"name\": \"Имя студента 1\", \"telegram\": \"Telegram-ник студента 1\"},\n" +
            "        {\"name\": \"Имя студента 2\", \"telegram\": \"Telegram-ник студента 2\"},\n" +
            "        ...\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"teamName\": \"Команда 2\",\n" +
            "      \"members\": [\n" +
            "        {\"name\": \"Имя студента 3\", \"telegram\": \"Telegram-ник студента 3\"},\n" +
            "        {\"name\": \"Имя студента 4\", \"telegram\": \"Telegram-ник студента 4\"},\n" +
            "        ...\n" +
            "      ]\n" +
            "    },\n" +
            "  ]\n" +
            "}\n" +
            "Нужны только данные без вводных фраз и объяснений. Не используй разметку Markdown!";

    @Override
    public YandexGptResponse apiTest() {

        var apiUrl = dotenv.get("YANDEX_API_URL");
        var apiKey = dotenv.get("YANDEX_API_KEY");
        var folderId = dotenv.get("YANDEX_FOLDER_ID");

        WebClient webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Api-Key " + apiKey)
                .defaultHeader("x-folder-id", folderId)
                .build();


        YandexGptRequest request = getYandexGptTestRequest(folderId);

        Mono<YandexGptResponse> responseMono = webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(YandexGptResponse.class);

        return responseMono.block();
    }

    private static YandexGptRequest getYandexGptTestRequest(String folderId) {
        var completionOptions = new CompletionOptions(false, 0.6, 2000);

        var systemMessage = new Message(
                "system",
                "Ты — умный ассистент.");

        var userMessage = new Message(
                "user",
                "Назови любые три группы товаров в продовольственном магазине. " +
                        "Для каждой группы приведи три любые подгруппы, входящие в группу. Представь " +
                        "результат в форме объекта JSON, где каждая группа товаров представлена в виде ключа в " +
                        "объекте JSON, а значениями являются массивы из соответствующих подгрупп. Нужны только данные " +
                        "без вводных фраз и объяснений. Не используй разметку Markdown!");

        return new YandexGptRequest(
                "gpt://"+ folderId +"/yandexgpt-lite/latest",
                completionOptions,
                List.of(systemMessage, userMessage));
    }

    @Override
    public YandexGptResponse getTeams(List<StudentInfoDto> students) {

        var apiUrl = dotenv.get("YANDEX_API_URL");
        var apiKey = dotenv.get("YANDEX_API_KEY");
        var folderId = dotenv.get("YANDEX_FOLDER_ID");

        WebClient webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Api-Key " + apiKey)
                .defaultHeader("x-folder-id", folderId)
                .build();
        YandexGptRequest request = new YandexGptRequest();
        try {
            request  = getYandexGptTeamRequest(folderId, students);
        }
        catch (JsonProcessingException ex){

        }

        Mono<YandexGptResponse> responseMono = webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(YandexGptResponse.class);

        return responseMono.block();
    }

    private static YandexGptRequest getYandexGptTeamRequest(String folderId, List<StudentInfoDto> students) throws JsonProcessingException {
        var completionOptions = new CompletionOptions(false, 0.6, 10000);

        var systemMessage = new Message(
                "system",
                gptCommand);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(students);

        var userMessage = new Message(
                "user",
                json);

        return new YandexGptRequest(
                "gpt://"+ folderId +"/yandexgpt/latest",
                completionOptions,
                List.of(systemMessage, userMessage));
    }
}
