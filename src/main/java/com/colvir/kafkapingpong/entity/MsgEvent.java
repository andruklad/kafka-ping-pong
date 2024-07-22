package com.colvir.kafkapingpong.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class MsgEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "sequence", allocationSize = 1)
    private Integer id;

    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private MsgType type;

    @Enumerated(EnumType.STRING)
    private MsgStatus status;

    @Type(JsonBinaryType.class)
    @Column(name = "data", columnDefinition = "jsonb")
    private String data;

    public MsgEvent() {
    }

    public MsgEvent(LocalDateTime dateTime, MsgType type, MsgStatus status, String data) {
        this.dateTime = dateTime;
        this.type = type;
        this.status = status;
        this.data = data;
    }
}
