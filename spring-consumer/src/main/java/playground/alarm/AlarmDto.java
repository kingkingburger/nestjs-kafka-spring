package playground.alarm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AlarmDto {
  private String alarmId;
  private String message;
  private java.time.LocalDateTime createdAt;
}