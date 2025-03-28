package org.example.service;

import org.example.model.dto.AuthenticationRequest;
import org.example.model.dto.AuthenticationResponse;
import org.example.model.dto.RegistrationStudentDto;

public interface AuthService {

  AuthenticationResponse authenticateUser(AuthenticationRequest request);

  void registerUserInKeycloak(RegistrationStudentDto registrationStudentDto);
}
