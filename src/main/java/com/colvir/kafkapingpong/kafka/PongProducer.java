package com.colvir.kafkapingpong.kafka;

import com.colvir.kafkapingpong.dto.KafkaMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PongProducer {

    private static final String PONG_OUT_BINDING_NAME = "pong-out-0";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final StreamBridge streamBridge;

    public void sendMsgToPongOutTopic(KafkaMessage kafkaMessage) {

        String textMsg;
        try {
            textMsg = objectMapper.writeValueAsString(kafkaMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Message<String> msg;
        msg = MessageBuilder
                .withPayload(textMsg)
                .setHeader("name", textMsg)
                .build();
        streamBridge.send(PONG_OUT_BINDING_NAME, msg);
    }
}
