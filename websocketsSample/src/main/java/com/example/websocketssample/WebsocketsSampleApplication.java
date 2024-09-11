package com.example.websocketssample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WebsocketsSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketsSampleApplication.class, args);
    }

}
