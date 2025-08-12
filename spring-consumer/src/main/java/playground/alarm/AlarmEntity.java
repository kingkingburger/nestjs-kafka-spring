package playground.alarm;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "alarm")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String alarmId;
  private String message;
  private LocalDateTime createdAt;

  /**
   * AlarmDto 객체를 받아서 AlarmEntity 객체를 생성하는 정적 팩토리 메서드입니다.
   *
   * @param dto 데이터 전송 객체
   * @return AlarmEntity 인스턴스
   */
  public static AlarmEntity fromDto(AlarmDto dto) {
    return AlarmEntity.builder()
        .alarmId(dto.getAlarmId())
        .message(dto.getMessage())
        .createdAt(dto.getCreatedAt())
        .build();
  }
}