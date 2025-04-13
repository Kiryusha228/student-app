package org.example.client;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.config.WebClientConfig;
import org.example.exception.StudentAuthenticationException;
import org.example.model.dto.auth.AuthenticationRequest;
import org.example.model.dto.keycloak.KeycloakRegistrationRequest;
import org.example.properties.KeycloakProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

@Component
@RequiredArgsConstructor
public class AuthClient {
  private final KeycloakProperties keycloakProperties;
  private final WebClientConfig webClient;

  private String getAdminToken() {
    var responseMono =
        webClient
            .getWebClient()
            .post()
            .uri(keycloakProperties.getAuthUrl())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(keycloakProperties.getAdminRequestUrl())
            .retrieve()
            .bodyToMono(Map.class);

    var responseBody = responseMono.block();

    if (responseBody == null || responseBody.isEmpty()) {
      throw new StudentAuthenticationException("Ошибка при авторизации администратора");
    }

    return (String) responseBody.get("access_token");
  }

  public void registerUser(KeycloakRegistrationRequest registrationRequest) {
    webClient
        .getWebClient()
        .post()
        .uri(keycloakProperties.getUserUrl())
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(registrationRequest)
        .retrieve()
        .bodyToMono(String.class)
        .block();
  }

  public Map authenticateUser(AuthenticationRequest request) {
    var requestBody = new LinkedMultiValueMap<String, String>();
    requestBody.add("client_id", keycloakProperties.getClientId());
    requestBody.add("username", request.getLogin());
    requestBody.add("password", request.getPassword());
    requestBody.add("grant_type", keycloakProperties.getGrantType());
    requestBody.add("client_secret", keycloakProperties.getClientSecret());

    var responseMono =
        webClient
            .getWebClient()
            .post()
            .uri(keycloakProperties.getTokenUrl())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(requestBody))
            .retrieve()
            .bodyToMono(Map.class);

    return responseMono.block();
  }
}
