package org.example.model.dto.yandexgpt.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletionOptions {
  boolean stream;
  double temperature;
  int maxTokens;
}
