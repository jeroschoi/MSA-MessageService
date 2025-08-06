package com.event.messageservice.service.kafka;

import lombok.Getter;

@Getter
public enum KafkaTopic {
    MESSAGE_SUCCESS(true,"message-success-topic"),
    MESSAGE_FAIL   (false,"message-fail-topic");

    private final boolean isSuccess;
    private final String topicName;

    KafkaTopic(boolean isSuccess,String name) {
        this.isSuccess = isSuccess;
        this.topicName = name;
    }

}