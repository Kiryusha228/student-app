package org.example.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.example.dto.kafka.KafkaRequestDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KafkaBotProducer {

    private final KafkaTemplate<String, KafkaRequestDto> kafkaTemplate;

    public void sendInferenceRequest(KafkaRequestDto dto) {
        kafkaTemplate.send("requests", dto);
    }
}
