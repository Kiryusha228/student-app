package org.example.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.kafka.KafkaRequestDto;
import org.example.model.dto.kafka.KafkaResultDto;
import org.example.service.YandexGptService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KafkaConsumer {

    private final YandexGptService yandexGptService;
    private final KafkaTemplate<String, KafkaResultDto> kafkaTemplate;

    @KafkaListener(topics = "requests", groupId = "student-app-group",
            containerFactory = "inferenceRequestKafkaListenerContainerFactory")
    public void consume(KafkaRequestDto dto) {
        try {
            yandexGptService.getTeams(dto.getTeamCount());
            var result = new KafkaResultDto();
            result.setChatId(dto.getChatId());
            result.setMessage("✅ Команды успешно сформированы!");

            kafkaTemplate.send("results", result);
        } catch (Exception e) {
            var result = new KafkaResultDto();
            result.setChatId(dto.getChatId());
            result.setMessage("❌ Произошла ошибка: " + e.getMessage());
            kafkaTemplate.send("results", result);
        }
    }
}