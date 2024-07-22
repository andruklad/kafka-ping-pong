package com.colvir.kafkapingpong.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PongReceiveEvent extends ApplicationEvent {

    private final Integer eventId;

    public PongReceiveEvent(Object source, Integer eventId) {
        super(source);
        this.eventId = eventId;
    }
}
