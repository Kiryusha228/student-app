package org.example.service.impl;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.example.exception.StudentAuthenticationException;
import org.example.model.dto.auth.AuthenticationRequest;
import org.example.model.dto.auth.AuthenticationResponse;
import org.example.model.dto.database.RegistrationStudentDto;
import org.example.model.dto.keycloak.KeycloakCredential;
import org.example.model.dto.keycloak.KeycloakRegistrationRequest;
import org.example.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final WebClient webClient;

    private final Dotenv dotenv;

    @Override
    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {

        var tokenUrl = dotenv.get("KEYCLOAK_TOKEN_URL");
        var grandType = dotenv.get("KEYCLOAK_GRANT_TYPE");
        var clientId = dotenv.get("KEYCLOAK_CLIENT_ID");
        var clientSecret = dotenv.get("KEYCLOAK_CLIENT_SECRET");

        var requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("client_id", clientId);
        requestBody.add("username", request.getLogin());
        requestBody.add("password", request.getPassword());
        requestBody.add("grant_type", grandType);
        requestBody.add("client_secret", clientSecret);

        var responseMono = webClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(requestBody))
                .retrieve()
                .bodyToMono(Map.class);

        var responseBody = responseMono.block();

        if (responseBody == null || responseBody.isEmpty()) {
            throw new StudentAuthenticationException("Ошибка при авторизации пользователя");
        }

        return new AuthenticationResponse((String) responseBody.get("access_token"));
    }

    @Override
    public void registerUserInKeycloak(RegistrationStudentDto registrationStudentDto) {

        var keycloakUrl = dotenv.get("KEYCLOAK_USER_URL");

        var credential = new ArrayList<KeycloakCredential>();

        credential.add(new KeycloakCredential("password", registrationStudentDto.getPassword(), false));

        var registrationRequest =
                new KeycloakRegistrationRequest(
                        registrationStudentDto.getName(), registrationStudentDto.getMail(), true, credential);


        //todo: посмотреть как здесь ловить ошибку

        webClient.post()
                .uri(keycloakUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registrationRequest)
                .retrieve()
                .bodyToMono(String.class).block();
    }

    private String getAdminToken() {
        var authUrl = dotenv.get("KEYCLOAK_AUTH_URL");
        var requestBody = dotenv.get("KEYCLOAK_ADMIN_REQUEST_URL");

        var responseMono = webClient
                .post()
                .uri(authUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class);

        var responseBody = responseMono.block();

        if (responseBody == null || responseBody.isEmpty()) {
            throw new StudentAuthenticationException("Ошибка при авторизации администратора");
        }

        return (String) responseBody.get("access_token");
    }
}
