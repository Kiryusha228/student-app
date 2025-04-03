package org.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.util.List;
import org.example.model.dto.database.StudentInfoDto;
import org.example.model.dto.database.TeamDto;
import org.example.model.dto.database.TeamListDto;
import org.example.model.dto.yandexgpt.Message;
import org.example.model.dto.yandexgpt.response.Alternative;
import org.example.model.dto.yandexgpt.response.Result;
import org.example.model.dto.yandexgpt.response.YandexGptResponse;
import org.example.service.impl.YandexGptServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class YandexGptServiceTest {
  @Mock private WebClient webClient;
  @Mock private WebClient.RequestHeadersSpec requestHeadersSpec;
  @Mock private WebClient.RequestBodySpec requestBodySpec;
  @Mock private WebClient.RequestBodyUriSpec requestBodyUriSpec;
  @Mock private WebClient.ResponseSpec responseSpec;

  @Mock private Dotenv dotenv;

  @Mock private ObjectMapper objectMapper;
  @Mock private ObjectReader objectReader;

  @Mock private TeamService teamService;

  @Mock private StudentProjectWorkshopService studentProjectWorkshopService;

  @Mock private FileReaderService fileReaderService;

  @InjectMocks private YandexGptServiceImpl yandexGptService;

  @Test
  void getTeams() {
    // Arrange
    var student1InfoDto = new StudentInfoDto();
    student1InfoDto.setStudentProjectWorkshopId(1L);
    var student2InfoDto = new StudentInfoDto();
    student2InfoDto.setStudentProjectWorkshopId(2L);

    var students = List.of(student1InfoDto, student2InfoDto);

    when(studentProjectWorkshopService.getAllPastStudents()).thenReturn(students);

    when(dotenv.get("YANDEX_API_URL")).thenReturn("fake-yandex-api-url");
    when(dotenv.get("YANDEX_API_KEY")).thenReturn("fake-api-key");
    when(dotenv.get("YANDEX_FOLDER_ID")).thenReturn("fake-folder-id");

    when(fileReaderService.getFromFile(anyString())).thenReturn("fake-gpt-command");

    when(webClient.post()).thenReturn(requestBodyUriSpec);
    when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
    when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
    when(requestBodySpec.contentType(any(MediaType.class))).thenReturn(requestBodySpec);
    when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

    var response = new YandexGptResponse(new Result());
    response.getResult().setAlternatives(List.of(new Alternative()));
    var text =
        """
                {
                  "teams": [
                    {
                      "students": [1, 2]
                    }
                  ]
                }""";
    response.getResult().getAlternatives().get(0).setMessage(new Message("role", text));

    when(responseSpec.bodyToMono(YandexGptResponse.class)).thenReturn(Mono.just(response));

    var teamDto = new TeamDto(List.of(1L, 2L));
    var expectedTeams = new TeamListDto(List.of(teamDto));

    when(objectMapper.reader()).thenReturn(objectReader);
    try {
      when(objectReader.readValue(text, TeamListDto.class)).thenReturn(expectedTeams);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Act
    yandexGptService.getTeams("2");

    // Assert
    verify(teamService).createTeams(expectedTeams);
  }
}
