package com.colvir.kafkapingpong.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KafkaMessage {

    private Integer id;

    private String dateTime;
}
