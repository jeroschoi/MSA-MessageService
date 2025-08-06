package com.event.messageservice.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = "message-success-topic", groupId = "message-group")
    public void consumeSuccess(String message) {
        log.info("Consumed success message: {}", message);
    }

    @KafkaListener(topics = "message-fail-topic", groupId = "message-group")
    public void consumeFail(String message) {
        log.info("Consumed fail message: {}", message);
    }
}
