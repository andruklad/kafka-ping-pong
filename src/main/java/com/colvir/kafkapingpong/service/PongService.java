package com.colvir.kafkapingpong.service;

import com.colvir.kafkapingpong.dto.KafkaMessage;
import com.colvir.kafkapingpong.entity.MsgType;
import com.colvir.kafkapingpong.event.PongReceiveEvent;
import com.colvir.kafkapingpong.kafka.PongProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PongService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MsgEventService msgEventService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private PongProducer pongProducer;

    @Autowired
    private MsgKafkaService msgKafkaService;

    @Transactional
    public void processMsgFromPing(String msg) {

        // Сохраняем событие получения сообщения от сервиса Ping в БД
        Integer msgEventId = savePongReceiveEvent(msg);

        // Регистрируем событие на уровне приложения
        publishPongReceiveEvent(msgEventId);

        // Записываем событие в лог
        logPongReceiveEvent(msg, msgEventId);
    }

    private Integer savePongReceiveEvent(String msg) {

        LocalDateTime currentDateTime = LocalDateTime.now();
        // Маппинг сообщения в KafkaMessage
        KafkaMessage kafkaMessage;
        try {
            kafkaMessage = objectMapper.readValue(msg, KafkaMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Integer msgEventId = msgEventService.saveMsgEvent(currentDateTime, MsgType.PING, kafkaMessage);
        return msgEventId;
    }

    private void publishPongReceiveEvent(Integer msgEventId) {

        PongReceiveEvent pongReceiveEvent = new PongReceiveEvent(this, msgEventId);
        applicationEventPublisher.publishEvent(pongReceiveEvent);
    }

    private void logPongReceiveEvent(String msg, Integer msgEventId) {

        System.out.printf("Msg %s, time: %s, event id: %s\n", msg, LocalDateTime.now(), msgEventId);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePongReceiveEvent(PongReceiveEvent pongReceiveEvent) {

        System.out.printf("handlePongReceiveEvent pongReceiveEvent: %s\n", pongReceiveEvent);
        pongMessage();
    }

    public void pongMessage() {

        KafkaMessage pongMessage = msgKafkaService.createKafkaMessage();
        pongProducer.sendMsgToPongOutTopic(pongMessage);
    }

}
