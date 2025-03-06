package org.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticationResponse {
    private String token;
}
