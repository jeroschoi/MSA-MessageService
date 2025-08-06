package com.event.messageservice.service;

import com.event.messageservice.dto.MessageRequestDto;
import com.event.messageservice.service.kafka.KafkaProducerService;
import com.event.messageservice.service.kafka.KafkaTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageSendService {

    private final MessageSenderAdapter adapter;
    private final MessageService messageSendService;
    private final KafkaProducerService kafkaProducerService;


    /**
     * 메시지 저장 및 전송
     */
    @Transactional
    public void sendMessage(MessageRequestDto dto) {
        String traceId = MDC.get("traceId");
        dto.setTraceId(traceId);

        try {
            log.info("메시지 전송 요청 - {}", dto);
            MessageSender messageSender = adapter.getMessageSender(dto);

            log.info("adapter.getMessageSender(dto) - {}", messageSender);
            messageSendService.saveMessageHistory(dto);
            messageSender.sendMessage(dto);
            log.info("메시지 전송 성공 - {}", dto);

            // Kafka 에 메시지 전송 성공 이벤트 발행
            kafkaProducerService.sendMessage(KafkaTopic.MESSAGE_SUCCESS, dto);

        } catch (Exception e) {
            log.error("메시지 전송 실패, 보상 트랜잭션 이벤트 발행 - {}", dto, e);
            // Kafka 에 메시지 전송 실패 이벤트 발행 (보상 트랜잭션)
            kafkaProducerService.sendMessage(KafkaTopic.MESSAGE_FAIL, dto);
            throw e; // 예외를 다시 던져 트랜잭션을 롤백
        }
    }

}
