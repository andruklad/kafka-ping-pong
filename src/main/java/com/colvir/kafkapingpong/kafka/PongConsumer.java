package com.colvir.kafkapingpong.kafka;

import com.colvir.kafkapingpong.service.MsgEventService;
import com.colvir.kafkapingpong.service.PongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class PongConsumer {

    @Autowired
    MsgEventService msgEventService;

    @Autowired
    PongService pongService;

    @Bean
    public Consumer<String> pongConsumerFunction() {

        return msg ->
            pongService.processMsgFromPing(msg);

    }
}
