package org.example.service;

import org.example.model.dto.auth.AuthenticationRequest;
import org.example.model.dto.auth.AuthenticationResponse;
import org.example.model.dto.database.RegistrationStudentDto;

public interface AuthService {

  AuthenticationResponse authenticateUser(AuthenticationRequest request);

  void registerUserInKeycloak(RegistrationStudentDto registrationStudentDto);
}
