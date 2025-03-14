package org.example.model.dto.yandexgpt.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.dto.yandexgpt.Message;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alternative {
    Message message;
    String status;
}
