package com.colvir.kafkapingpong.kafka;

import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class KafkaProducer {

    private static final String CITY_OUT_BINDING_NAME = "ping-out-0";

    private final StreamBridge streamBridge;

    @Scheduled(fixedDelay = 20000)
    public void sendMsgToPingTopic() {

        String textMsg = "test sendMsgToPingTopic" + LocalDateTime.now();
        Message<String> msg;
        msg = MessageBuilder
                .withPayload(textMsg)
                .setHeader("name", textMsg)
                .build();
        streamBridge.send(CITY_OUT_BINDING_NAME, msg);
    }
}
