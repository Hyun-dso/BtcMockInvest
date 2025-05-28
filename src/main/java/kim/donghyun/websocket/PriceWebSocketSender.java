package kim.donghyun.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PriceWebSocketSender {

    private final SimpMessagingTemplate messagingTemplate;

    public void broadcast(double price) {
        messagingTemplate.convertAndSend("/topic/price", price);
    }
}
