package org.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
  private final DotenvConfig dotenv;

  @Bean
  public WebClient getWebClient() {
    return WebClient.builder().baseUrl(dotenv.getDotenv().get("APP_API_URL")).build();
  }
}
