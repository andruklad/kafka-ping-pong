package com.colvir.kafkapingpong.service;

import com.colvir.kafkapingpong.config.Config;
import com.colvir.kafkapingpong.dto.KafkaMessage;
import com.colvir.kafkapingpong.entity.MsgType;
import com.colvir.kafkapingpong.kafka.PingProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PingService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MsgKafkaService msgKafkaService;

    private final MsgEventService msgEventService;

    private final PingProducer pingProducer;

    private final Config config;

    @Scheduled(fixedDelayString = "${app.pingMessageDelay}")
    public void pingMessage() {

        if (config.getEnablePingMessage() == 1) {
            KafkaMessage pingMessage = msgKafkaService.createKafkaMessage();
            pingProducer.sendMsgToPingOutTopic(pingMessage);
        }
    }

    @Transactional
    public void processMsgFromPong(String msg) {

        // Сохраняем в БД событие получения сообщения от сервиса Ping
        Integer msgEventId = savePingReceiveEvent(msg);

        // Записываем событие в лог
        logPingReceiveEvent(msg, msgEventId);
    }

    private Integer savePingReceiveEvent(String msg) {

        LocalDateTime currentDateTime = LocalDateTime.now();
        // Маппинг сообщения в KafkaMessage
        KafkaMessage kafkaMessage;
        try {
            kafkaMessage = objectMapper.readValue(msg, KafkaMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Integer msgEventId = msgEventService.saveMsgEvent(currentDateTime, MsgType.PONG, kafkaMessage);
        return msgEventId;
    }

    private void logPingReceiveEvent(String msg, Integer msgEventId) {

        System.out.printf("Log logPingReceiveEvent. Msg %s, time: %s, event id: %s\n", msg, LocalDateTime.now(), msgEventId);
    }
}
