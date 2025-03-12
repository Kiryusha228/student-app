package org.example.model.dto.keycloak;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class KeycloakRegistrationRequest {
  private String username;
  private String email;
  private boolean enabled;
  private List<KeycloakCredential> credentials;
}
