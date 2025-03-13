package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.mapper.StudentMapper;
import org.example.model.dto.auth.AuthenticationRequest;
import org.example.model.dto.auth.AuthenticationResponse;
import org.example.model.dto.RegistrationStudentDto;
import org.example.service.AuthService;
import org.example.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final StudentService studentService;
    private final StudentMapper studentMapper;

    private final JwtDecoder jwtDecoder;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegistrationStudentDto registrationStudentDto) {

        authService.registerUserInKeycloak(registrationStudentDto);

        var token = authService.authenticateUser(new AuthenticationRequest(
                        registrationStudentDto.getName(),
                        registrationStudentDto.getPassword()
                )
        );

        studentService.createStudent(
                studentMapper.registrationStudentDtoToStudentEntity(
                        registrationStudentDto,
                        jwtDecoder.decode(token.getToken()).getSubject()
                )
        );
        return ResponseEntity.ok(token);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticateUser(request));
    }


}


