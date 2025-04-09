package org.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.cors(Customizer.withDefaults())
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                    .requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/api/auth/**",
                        "/api/test/add",
                        "/api/test/get",
                        "/api/test/delete",
                        "/api/student/**",
                        "/api/gpt/**",
                        "/api/test-result/get",
                        "/api/student-project-workshop/info",
                        "/api/student-project-workshop/get-all-students",
                        "/api/student-project-workshop/get-all-past-students",
                        "/api/student-project-workshop/get-by-name",
                        "/api/student-project-workshop/get-by-id",
                        "/api/student-project-workshop/get-by-telegram",
                        "/api/project-workshop/**",
                        "/api/hr/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
        .csrf(AbstractHttpConfigurer::disable);

    return http.build();
  }
}
