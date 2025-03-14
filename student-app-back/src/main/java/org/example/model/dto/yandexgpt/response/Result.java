package org.example.model.dto.yandexgpt.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    List<Alternative> alternatives;
    Usage usage;
    String modelVersion;
}
