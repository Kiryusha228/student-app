package org.example.model.dto.yandexgpt.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.dto.yandexgpt.Message;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class YandexGptRequest {
  String modelUri;
  CompletionOptions completionOptions;
  List<Message> messages;
}
