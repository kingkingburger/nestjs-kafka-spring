package playground.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Kafka에서 직접 수신하는 원본 메시지 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class KafkaMessageDto {

  private long id;
  private String text;

}