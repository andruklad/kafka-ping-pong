package com.colvir.kafkapingpong.kafka;

import com.colvir.kafkapingpong.service.PingService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@AllArgsConstructor
public class PingConsumer {

    private final PingService pingService;

    @Bean
    public Consumer<String> pingConsumerFunction() {

        return pingService::processMsgFromPong;
    }
}
