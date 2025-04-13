package org.example.client;

import lombok.RequiredArgsConstructor;
import org.example.config.WebClientConfig;
import org.example.model.dto.yandexgpt.request.YandexGptRequest;
import org.example.model.dto.yandexgpt.response.YandexGptResponse;
import org.example.properties.YandexProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class YandexClient {
  private final WebClientConfig webClientConfig;
  private final YandexProperties yandexProperties;

  public YandexGptResponse createTeams(YandexGptRequest request) {
    Mono<YandexGptResponse> responseMono =
        webClientConfig
            .getWebClient()
            .post()
            .uri(yandexProperties.getApiUrl())
            .header(HttpHeaders.AUTHORIZATION, "Api-Key " + yandexProperties.getApiKey())
            .header("x-folder-id", yandexProperties.getFolderId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(YandexGptResponse.class);
    return responseMono.block();
  }
}
