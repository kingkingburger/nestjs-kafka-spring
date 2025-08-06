package com.example.demo;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = "my-first-topic", groupId = "spring-consumer-group")
    public void consume(String message) throws IOException {
        System.out.println(String.format("Consumed message: %s", message));
    }
}