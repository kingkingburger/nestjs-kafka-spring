package com.example.demo;

import java.io.IOException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

  @KafkaListener(topics = "my-first-topic", groupId = "spring-consumer-group")
  public void consume(String message) throws IOException {
    System.out.println(String.format("Consumed message: %s", message));
  }
}