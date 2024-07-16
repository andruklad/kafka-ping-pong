package com.colvir.kafkapingpong.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Consumer;

@Component
public class KafkaConsumer {

    @Bean
    public Consumer<String> pongConsumer() {

        return msg ->
                System.out.printf("Msg %s, time: %s\n", msg, LocalDateTime.now());
    }
}
