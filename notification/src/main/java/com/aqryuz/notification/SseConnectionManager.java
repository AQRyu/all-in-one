package com.aqryuz.notification;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseConnectionManager {

  private static final Map<String, SseEmitter> connections = new ConcurrentHashMap<>();

  public void addConnection(String userId, SseEmitter emitter) {
    connections.put(userId, emitter);
  }

  public void removeConnection(String userId) {
    connections.remove(userId);
  }

  public SseEmitter getConnection(String userId) {
    return connections.get(userId);
  }

  public void sendToUserId(String userId, String message) {
    SseEmitter emitter = getConnection(userId);
    if (emitter != null) {
      try {
        emitter.send("Event: message, Data: " + message);
      } catch (IOException e) {
        // Handle error, e.g., remove connection
        removeConnection(userId);
      }
    }
  }
}
