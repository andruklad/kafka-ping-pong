package com.colvir.kafkapingpong.service;

import com.colvir.kafkapingpong.dto.KafkaMessage;
import com.colvir.kafkapingpong.kafka.PingProducer;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PingService {

    @Autowired
    private MsgKafkaService msgKafkaService;

    @Autowired
    private PingProducer pingProducer;

    @Scheduled(fixedDelay = 500000)
    public void pingMessage() {

        KafkaMessage pingMessage = msgKafkaService.createKafkaMessage();
        pingProducer.sendMsgToPingOutTopic(pingMessage);
    }

    @Transactional
    public void processMsgFromPong(String msg) {

        // Сохраняем событие получения сообщения от сервиса Ping в БД
//        Integer msgEventId = savePongReceiveEvent(msg);

        // Регистрируем событие на уровне приложения
//        publishPongReceiveEvent(msgEventId);

        // Записываем событие в лог
        System.out.printf("Msg %s, time: %s, event id: %s\n", msg, LocalDateTime.now(), null);
//        logPongReceiveEvent(msg, msgEventId);
    }

//    private void logPongReceiveEvent(String msg, Integer msgEventId) {
//
//        System.out.printf("Msg %s, time: %s, event id: %s\n", msg, LocalDateTime.now(), msgEventId);
//    }
}
