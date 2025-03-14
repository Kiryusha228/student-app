package org.example.model.dto.yandexgpt.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.dto.yandexgpt.Message;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class YandexGptRequest {
    String modelUri;
    CompletionOptions completionOptions;
    List<Message> messages;
}
