package com.colvir.kafkapingpong.service;

import com.colvir.kafkapingpong.config.Config;
import com.colvir.kafkapingpong.dto.KafkaMessage;
import com.colvir.kafkapingpong.entity.MsgEvent;
import com.colvir.kafkapingpong.entity.MsgType;
import com.colvir.kafkapingpong.event.PongReceiveEvent;
import com.colvir.kafkapingpong.kafka.PongProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PongService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MsgEventService msgEventService;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final PongProducer pongProducer;

    private final MsgKafkaService msgKafkaService;

    private final Config config;

    @Transactional
    public void processMsgFromPing(String msg) {

        // Сохраняем событие получения сообщения от сервиса Ping в БД
        Integer msgEventId = savePongReceiveEvent(msg);

        // Регистрируем событие на уровне приложения
        if (config.getEnablePublishPongReceiveEvent() == 1) {
            publishPongReceiveEvent(msgEventId);
        }

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

        System.out.printf("Log logPongReceiveEvent. Msg %s, time: %s, event id: %s\n", msg, LocalDateTime.now(), msgEventId);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePongReceiveEvent(PongReceiveEvent pongReceiveEvent) {

        System.out.printf("handlePongReceiveEvent pongReceiveEvent: %s\n", pongReceiveEvent);
        pongMessage();
        msgEventService.updateStatusToProcessed(pongReceiveEvent.getEventId());
    }

    public void pongMessage() {

        KafkaMessage pongMessage = msgKafkaService.createKafkaMessage();
        pongProducer.sendMsgToPongOutTopic(pongMessage);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void listenerContext() {

        // Получение списка новых событий типа PING из БД
        List<MsgEvent> eventsByTypePing = msgEventService.getNewEventsByType(MsgType.PING);
        // Маппинг в список событий для публикации в приложении
        List<PongReceiveEvent> pongReceiveEvents = eventsByTypePing.stream()
                .map(ev -> new PongReceiveEvent(this, ev.getId())).toList();
        // Публикация событий в приложении
        for (PongReceiveEvent pongReceiveEvent: pongReceiveEvents) {
            applicationEventPublisher.publishEvent(pongReceiveEvent);
        }
    }

}
