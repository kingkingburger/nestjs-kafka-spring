package playground.sse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

// 컨트롤러 대신 Sse 로직을 담당하는 별도 컴포넌트로 분리하여 재사용성을 높임
@Component
public class SseEmitterManager {

  // 클라이언트별 Emitter를 저장하는 저장소
  private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

  // 클라이언트가 구독을 요청하면 호출됨
  public SseEmitter subscribe(String clientId) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // Timeout을 매우 길게 설정

    // 연결이 끊어지거나 타임아웃되면 저장소에서 제거
    emitter.onCompletion(() -> emitters.remove(clientId));
    emitter.onTimeout(() -> emitters.remove(clientId));

    emitters.put(clientId, emitter);

    // 연결 확인을 위한 초기 데이터 전송
    try {
      emitter.send(SseEmitter.event().name("connect").data("Connected! clientId=" + clientId));
    } catch (IOException e) {
      emitter.completeWithError(e);
    }

    return emitter;
  }

  // 모든 클라이언트에게 알람을 방송(Broadcast)하는 메서드
  public void broadcast(Object alarmData) {
    // emitters 맵의 모든 SseEmitter 객체에 대해 반복
    emitters.forEach((id, emitter) -> {
      try {
        emitter.send(SseEmitter.event().name("alarm").data(alarmData));
      } catch (IOException e) {
        // 클라이언트와의 연결이 끊겼을 경우, IOException이 발생
        System.err.println("Error sending to client " + id + ": " + e.getMessage());
        // 해당 emitter를 맵에서 제거
        emitters.remove(id);
      }
    });
  }
}


