package org.example.model.dto.keycloak;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class KeycloakCredential {
    private String type;
    private String value;
    private boolean temporary;
}
