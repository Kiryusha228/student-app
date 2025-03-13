package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.auth.AuthenticationRequest;
import org.example.model.dto.auth.AuthenticationResponse;
import org.example.model.dto.RegistrationStudentDto;
import org.example.model.dto.keycloak.KeycloakCredential;
import org.example.model.dto.keycloak.KeycloakRegistrationRequest;
import org.example.service.AuthService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Dotenv dotenv = Dotenv.configure().load();

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {

        var tokenUrl = dotenv.get("KEYCLOAK_TOKEN_URL");
        var grandType = dotenv.get("KEYCLOAK_GRANT_TYPE");
        var clientId = dotenv.get("KEYCLOAK_CLIENT_ID");
        var clientSecret = dotenv.get("KEYCLOAK_CLIENT_SECRET");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = String.format(
                "client_id=%s&username=%s&password=%s&grant_type=%s&client_secret=%s",
                clientId, request.getLogin(), request.getPassword(), grandType, clientSecret
        );

        HttpEntity<String> httpRequest = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                tokenUrl,
                httpRequest,
                Map.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return new AuthenticationResponse((String) response.getBody().get("access_token"));
        } else {
            return null;
        }
    }

    @Override
    public void registerUserInKeycloak(RegistrationStudentDto registrationStudentDto) {

        var keycloakUrl = dotenv.get("KEYCLOAK_USER_URL");

        var credential = new ArrayList<KeycloakCredential>();

        credential.add(new KeycloakCredential(
                "password",
                registrationStudentDto.getPassword(),
                false)
        );

        var registrationRequest = new KeycloakRegistrationRequest(
                registrationStudentDto.getName(),
                registrationStudentDto.getMail(),
                true,
                credential
        );

        String adminToken = getAdminToken();

        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(adminToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<KeycloakRegistrationRequest> httpEntityRequest = new HttpEntity<>(registrationRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                keycloakUrl,
                httpEntityRequest,
                String.class
        );
    }

    private String getAdminToken() {
        var authUrl = dotenv.get("KEYCLOAK_AUTH_URL");
        var requestBody = dotenv.get("KEYCLOAK_ADMIN_REQUEST_URL");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(authUrl, request, Map.class);
        return (String) response.getBody().get("access_token");
    }
}
