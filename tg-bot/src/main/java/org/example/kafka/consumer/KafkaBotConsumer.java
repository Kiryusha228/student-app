package org.example.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.example.dto.kafka.KafkaResultDto;
import org.example.service.TgBotService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RequiredArgsConstructor
@Service
public class KafkaBotConsumer {

    private final TgBotService tgBotService;

    @KafkaListener(topics = "results", groupId = "student-app-group",
            containerFactory = "inferenceResultKafkaListenerContainerFactory")
    public void consume(KafkaResultDto dto) {
        SendMessage message = new SendMessage();
        message.setChatId(dto.getChatId());
        message.setText(dto.getMessage());

        try {
            tgBotService.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

