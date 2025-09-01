package playground.alarm;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import playground.sse.SseEmitterManager;

@Service
public class AlarmService {

  private static final String CONFIRM_TOPIC = "alarm-confirm";
  private final AlarmRepository alarmRepository;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final SseEmitterManager sseEmitterManager;

  @Autowired
  public AlarmService(AlarmRepository alarmRepository,
      KafkaTemplate<String, String> kafkaTemplate,
      SseEmitterManager sseEmitterManager) {
    this.alarmRepository = alarmRepository;
    this.kafkaTemplate = kafkaTemplate;
    this.sseEmitterManager = sseEmitterManager;
  }

  @Transactional
  public void processAlarm(AlarmDto alarmDto) {
    System.out.println("Received alarm: " + alarmDto);

    // 1. DTO를 Entity로 변환하여 DB에 저장
    AlarmEntity alarm = AlarmEntity.fromDto(alarmDto);
    alarmRepository.save(alarm);
    System.out.println("Alarm saved to PostgreSQL.");

    // 2. SSE를 통해 모든 클라이언트에게 실시간 알람 전송
    sseEmitterManager.broadcast(alarmDto);
    System.out.println("Sent real-time alarm via SSE to user: " + alarmDto.getAlarmId());

    // 3. Kafka에 확인 메시지 전송
    String confirmationMessage =
        "Alarm with ID " + alarmDto.getAlarmId() + " has been successfully processed.";
    kafkaTemplate.send(CONFIRM_TOPIC, confirmationMessage);
    System.out.println("Confirmation message sent to topic: " + CONFIRM_TOPIC);
  }
}