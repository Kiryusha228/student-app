package org.example.properties;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class KeycloakProperties {
  String tokenUrl;
  String grantType;
  String clientId;
  String clientSecret;
  String userUrl;
  String authUrl;
  String adminRequestUrl;

  KeycloakProperties() {
    this.tokenUrl = System.getenv("KEYCLOAK_TOKEN_URL");
    this.grantType = System.getenv("KEYCLOAK_GRANT_TYPE");
    this.clientId = System.getenv("KEYCLOAK_CLIENT_ID");
    this.clientSecret = System.getenv("KEYCLOAK_CLIENT_SECRET");
    this.userUrl = System.getenv("KEYCLOAK_USER_URL");
    this.authUrl = System.getenv("KEYCLOAK_AUTH_URL");
    this.adminRequestUrl = System.getenv("KEYCLOAK_ADMIN_REQUEST_URL");
  }
}
