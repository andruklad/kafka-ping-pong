package com.colvir.kafkapingpong.service;

import com.colvir.kafkapingpong.dto.KafkaMessage;
import com.colvir.kafkapingpong.entity.MsgEvent;
import com.colvir.kafkapingpong.entity.MsgStatus;
import com.colvir.kafkapingpong.entity.MsgType;
import com.colvir.kafkapingpong.repository.MsgEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class MsgEventService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MsgEventRepository msgEventRepository;

    public MsgEventService(MsgEventRepository msgEventRepository) {
        this.msgEventRepository = msgEventRepository;
    }

    public Integer saveMsgEvent(LocalDateTime currentDateTime, MsgType msgType, KafkaMessage kafkaMessage) {

        try {
            MsgEvent event = msgEventRepository.save(new MsgEvent(
                    currentDateTime,
                    msgType,
                    MsgStatus.NEW,
                    objectMapper.writeValueAsString(kafkaMessage)
            ));
            return event.getId();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

//    public void updateStatusToProcessed(Integer id) {
//
//        MsgEvent event = msgEventRepository.findById(id).get();
//        event.setStatus(MsgStatus.PROCESSED);
//        msgEventRepository.save(event);
//    }
//
//    public List<MsgEvent> getNewEventsByType(MsgType msgType) {
//
//        return msgEventRepository.findAllByTypeAndStatus(msgType, MsgStatus.NEW);
//    }

}
