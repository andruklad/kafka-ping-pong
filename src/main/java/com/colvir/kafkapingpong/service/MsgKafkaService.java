package com.colvir.kafkapingpong.service;

import com.colvir.kafkapingpong.dto.KafkaMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class MsgKafkaService {

    private Integer generateId4KafkaMessage() {

        Random random = new Random();
        return random.nextInt();
    }

    public KafkaMessage createKafkaMessage() {

        return new KafkaMessage(generateId4KafkaMessage(), LocalDateTime.now().toString());
    }
}
