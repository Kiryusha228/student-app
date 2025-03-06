package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.AuthenticationRequest;
import org.example.model.dto.AuthenticationResponse;
import org.example.model.entity.StudentEntity;
import org.example.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public  ResponseEntity<AuthenticationResponse> registerUser(@RequestBody StudentEntity studentEntity) {
        authService.registerUserInKeycloak(studentEntity);
        return ResponseEntity.ok(authService.authenticateUser(new AuthenticationRequest(studentEntity.getName(), studentEntity.getPassword())));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticateUser(request));
    }


}


