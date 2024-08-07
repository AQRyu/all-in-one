package com.aqryuz.notification;

import java.io.IOException;
import java.util.concurrent.Executors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class SseController {

  @GetMapping("/stream-sse")
  public SseEmitter handleSse() {
    SseEmitter emitter = new SseEmitter();
    Executors.newSingleThreadExecutor()
        .execute(
            () -> {
              try {
                for (int i = 1; i <= 5; i++) {
                  emitter.send("Event: message, Data: " + i);
                  Thread.sleep(1000);
                }
                emitter.complete();
              } catch (IOException e) {
                emitter.completeWithError(e);
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupt status
                emitter.completeWithError(new RuntimeException("SSE emitter interrupted", e));
              }
            });
    return emitter;
  }
}
