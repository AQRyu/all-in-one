package com.aqryuz.notification;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class NotificationController {

  private final SseConnectionManager connectionManager;

  public NotificationController(SseConnectionManager connectionManager) {
    this.connectionManager = connectionManager;
  }

  @GetMapping("/connect/{userId}")
  public SseEmitter connect(@PathVariable String userId) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // Keep-alive
    connectionManager.addConnection(userId, emitter);

    // Handle connection closure (remove from map)
    emitter.onCompletion(() -> connectionManager.removeConnection(userId));
    emitter.onTimeout(() -> connectionManager.removeConnection(userId));
    emitter.onError(ex -> connectionManager.removeConnection(userId));

    return emitter;
  }

  // Example: Send to a specific user
  @PostMapping("/notify/{userId}")
  public void notifyUser(@PathVariable String userId, @RequestBody String message) {
    connectionManager.sendToUserId(userId, message);
  }
}
