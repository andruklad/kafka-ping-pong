package com.colvir.kafkapingpong.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class Config {

    private final Integer enablePingMessage;

    private final Integer enablePublishPongReceiveEvent;

    public Config(
            @Value("${app.enablePingMessage}") Integer enablePingMessage,
            @Value("${app.enablePublishPongReceiveEvent}") Integer enablePublishPongReceiveEvent
    ) {
        this.enablePingMessage = enablePingMessage;
        this.enablePublishPongReceiveEvent = enablePublishPongReceiveEvent;
    }
}
