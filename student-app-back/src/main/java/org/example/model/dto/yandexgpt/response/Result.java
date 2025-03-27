package org.example.model.dto.yandexgpt.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
  List<Alternative> alternatives;
  Usage usage;
  String modelVersion;
}
