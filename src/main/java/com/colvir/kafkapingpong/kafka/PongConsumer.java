package com.colvir.kafkapingpong.kafka;

import com.colvir.kafkapingpong.service.PongService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@AllArgsConstructor
public class PongConsumer {

    private final PongService pongService;

    @Bean
    public Consumer<String> pongConsumerFunction() {

        return pongService::processMsgFromPing;
    }
}
