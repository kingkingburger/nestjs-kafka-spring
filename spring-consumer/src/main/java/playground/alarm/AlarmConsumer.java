package playground.alarm;

import java.io.IOException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AlarmConsumer {

  @KafkaListener(topics = "alarm", groupId = "alarm-group")
  public void consume(String message) throws IOException {
    System.out.println(String.format("알람: %s", message));
  }
}
