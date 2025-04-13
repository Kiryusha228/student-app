package org.example.service.impl;

import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.example.client.AuthClient;
import org.example.exception.StudentAuthenticationException;
import org.example.model.dto.auth.AuthenticationRequest;
import org.example.model.dto.auth.AuthenticationResponse;
import org.example.model.dto.database.RegistrationStudentDto;
import org.example.model.dto.keycloak.KeycloakCredential;
import org.example.model.dto.keycloak.KeycloakRegistrationRequest;
import org.example.service.AuthService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final AuthClient authClient;

  @Override
  public AuthenticationResponse authenticateUser(AuthenticationRequest request) {
    var responseBody = authClient.authenticateUser(request);

    if (responseBody == null || responseBody.isEmpty()) {
      throw new StudentAuthenticationException("Ошибка при авторизации пользователя");
    }

    return new AuthenticationResponse((String) responseBody.get("access_token"));
  }

  @Override
  public void registerUserInKeycloak(RegistrationStudentDto registrationStudentDto) {

    var credential = new ArrayList<KeycloakCredential>();

    credential.add(new KeycloakCredential("password", registrationStudentDto.getPassword(), false));

    var registrationRequest =
        new KeycloakRegistrationRequest(
            registrationStudentDto.getName(), registrationStudentDto.getMail(), true, credential);

    authClient.registerUser(registrationRequest);
  }
}
