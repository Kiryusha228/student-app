package org.example.model.dto.yandexgpt.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usage {
    String inputTextTokens;
    String completionTokens;
    String totalTokens;
    Object completionTokensDetails;
}
