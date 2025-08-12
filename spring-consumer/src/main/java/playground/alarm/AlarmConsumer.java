package playground.alarm;

import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import playground.dto.KafkaMessageDto;

@Component
@RequiredArgsConstructor
public class AlarmConsumer {

  private final AlarmService alarmService;

  @KafkaListener(topics = "alarm", groupId = "spring-consumer-group")
  public void consume(KafkaMessageDto kafkaMessage) throws IOException {
    System.out.println("수신된 원본 메시지: " + kafkaMessage);

    // KafkaMessageDto -> AlarmDto 로 변환
    AlarmDto alarmDto = new AlarmDto();
    alarmDto.setAlarmId(String.valueOf(kafkaMessage.getId())); // long -> String 변환
    alarmDto.setMessage(kafkaMessage.getText());
    alarmDto.setCreatedAt(LocalDateTime.now()); // 수신한 시각을 생성 시각으로 설정

    System.out.println("변환된 AlarmDto: " + alarmDto);

    // 변환된 DTO를 사용하여 비즈니스 로직 처리
    alarmService.processAlarm(alarmDto);
  }
}
