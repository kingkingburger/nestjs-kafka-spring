package playground;

import java.io.IOException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import playground.dto.KafkaMessageDto;

@Component
public class KafkaConsumer {

  @KafkaListener(topics = "my-first-topic", groupId = "spring-consumer-group")
  public void consume(KafkaMessageDto kafkaMessage) throws IOException {
    System.out.println("수신된 원본 메시지: " + kafkaMessage);
  }
}