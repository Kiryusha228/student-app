package org.example.model.dto.keycloak;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class KeycloakRegistrationRequest {
    private String username;
    private String email;
    private boolean enabled;
    private List<KeycloakCredential> credentials;
}
