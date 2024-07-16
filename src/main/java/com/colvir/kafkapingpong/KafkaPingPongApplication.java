package com.colvir.kafkapingpong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaPingPongApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaPingPongApplication.class, args);
    }

}
