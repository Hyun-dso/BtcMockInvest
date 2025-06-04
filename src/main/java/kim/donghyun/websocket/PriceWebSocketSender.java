package kim.donghyun.websocket;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PriceWebSocketSender {

    private final SimpMessagingTemplate messagingTemplate;

    public void broadcast(double price) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("price", price);
        payload.put("timestamp", Instant.now().getEpochSecond()); // ✅ UTC 정확하게 맞음

        messagingTemplate.convertAndSend("/topic/price", payload);
    }
}
