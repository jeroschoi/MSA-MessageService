package com.event.messageservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaProducerTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Kafka 메시지 발행 메소드
     * @param topic Kafka 토픽
     * @param data 발행할 데이터
     * @param <T> 데이터 타입
     */
    public <T> void sendMessage(KafkaTopic topic, T data) {
        try {
            String json = objectMapper.writeValueAsString(data);
            kafkaProducerTemplate.send(topic.getTopicName(), json);
            log.info("Kafka 메시지 발행 성공 - Topic: {}, Data: {}", topic.getTopicName(), json);
        } catch (JsonProcessingException e) {
            log.error("JSON 데이터 직렬화 실패", e);
        } catch (Exception e) {
            log.error("Kafka 메시지 발행 실패", e);
        }
    }
}
