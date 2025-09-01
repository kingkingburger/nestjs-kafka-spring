package playground.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@CrossOrigin(origins = "http://localhost:5500")
@RequiredArgsConstructor
// 웹 요청을 받는 컨트롤러
@RestController
@RequestMapping("/api/sse")
class SseController {

  private final SseEmitterManager sseEmitterManager;

  // 클라이언트가 SSE 구독을 요청하는 엔드포인트
  @GetMapping(value = "/subscribe/{userId}", produces = "text/event-stream")
  public SseEmitter subscribe(@PathVariable("userId") String userId) {
    return sseEmitterManager.subscribe(userId);
  }
}
