package org.example.client;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.config.WebClientConfig;
import org.example.dto.CreateProjectWorkshopDto;
import org.example.dto.ProjectWorkshopDto;
import org.example.dto.StudentInfoDto;
import org.example.dto.TeamWithStudentInfoDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentAppClient {
  private final WebClientConfig webClient;

  public void addProjectWorkshop(CreateProjectWorkshopDto createProjectWorkshopDto) {
    webClient
        .getWebClient()
        .post()
        .uri("project-workshop/add")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(createProjectWorkshopDto)
        .retrieve()
        .bodyToMono(Void.class)
        .block();
  }

  public void enableProjectWorkshop(Long projectWorkshopId) {
    webClient
        .getWebClient()
        .post()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("project-workshop/enable")
                    .queryParam("projectWorkshopId", projectWorkshopId)
                    .build())
        .retrieve()
        .bodyToMono(Void.class)
        .block();
  }

  public void disableProjectWorkshop(Long projectWorkshopId) {
    webClient
        .getWebClient()
        .post()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("project-workshop/disable")
                    .queryParam("projectWorkshopId", projectWorkshopId)
                    .build())
        .retrieve()
        .bodyToMono(Void.class)
        .block();
  }

  public List<TeamWithStudentInfoDto> getTeamsByProjectWorkshopId(Long projectWorkshopId) {
    return webClient
        .getWebClient()
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("project-workshop/team")
                    .queryParam("projectWorkshopId", projectWorkshopId)
                    .build())
        .retrieve()
        .bodyToFlux(TeamWithStudentInfoDto.class)
        .collectList()
        .block();
  }

  public ProjectWorkshopDto getLastProjectWorkshop() {
    return webClient
        .getWebClient()
        .get()
        .uri("project-workshop/get/last")
        .retrieve()
        .bodyToMono(ProjectWorkshopDto.class)
        .block();
  }

  public ProjectWorkshopDto getProjectWorkshopById(Long projectWorkshopId) {
    return webClient
        .getWebClient()
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("project-workshop/get/id")
                    .queryParam("projectWorkshopId", projectWorkshopId)
                    .build())
        .retrieve()
        .bodyToMono(ProjectWorkshopDto.class)
        .block();
  }

  public List<ProjectWorkshopDto> getAllProjectWorkshops() {
    return webClient
        .getWebClient()
        .get()
        .uri("project-workshop/get/all")
        .retrieve()
        .bodyToFlux(ProjectWorkshopDto.class)
        .collectList()
        .block();
  }

  public StudentInfoDto getStudentProjectWorkshopByTelegram(String telegram) {
    return webClient
        .getWebClient()
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("student-project-workshop/get-by-telegram")
                    .queryParam("telegram", telegram)
                    .build())
        .retrieve()
        .bodyToMono(StudentInfoDto.class)
        .block();
  }

  public StudentInfoDto getStudentProjectWorkshopByName(String name) {
    return webClient
        .getWebClient()
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("student-project-workshop/get-by-name")
                    .queryParam("name", name)
                    .build())
        .retrieve()
        .bodyToMono(StudentInfoDto.class)
        .block();
  }

  public List<StudentInfoDto> getAllStudentsProjectWorkshopByName(String name) {
    return webClient
        .getWebClient()
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("student-project-workshop/get-all-by-name")
                    .queryParam("name", name)
                    .build())
        .retrieve()
        .bodyToFlux(StudentInfoDto.class)
        .collectList()
        .block();
  }

  public StudentInfoDto getStudentProjectWorkshopById(Long id) {
    return webClient
        .getWebClient()
        .get()
        .uri(
            uriBuilder ->
                uriBuilder.path("student-project-workshop/get-by-id").queryParam("id", id).build())
        .retrieve()
        .bodyToMono(StudentInfoDto.class)
        .block();
  }

  public List<StudentInfoDto> getAllStudentsByProjectWorkshopId(Long projectWorkshopId) {
    return webClient
        .getWebClient()
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("student-project-workshop/get-all-students")
                    .queryParam("projectWorkshopId", projectWorkshopId)
                    .build())
        .retrieve()
        .bodyToFlux(StudentInfoDto.class)
        .collectList()
        .block();
  }

  public List<StudentInfoDto> getAllPastStudentsOnLastProjectWorkshop() {
    return webClient
        .getWebClient()
        .get()
        .uri("student-project-workshop/get-all-past-students")
        .retrieve()
        .bodyToFlux(StudentInfoDto.class)
        .collectList()
        .block();
  }

  public void createTeams(String teamCount) {
    webClient
        .getWebClient()
        .post()
        .uri(uriBuilder -> uriBuilder.path("gpt/teams").queryParam("teamCount", teamCount).build())
        .retrieve()
        .bodyToMono(Void.class)
        .block();
  }
}
