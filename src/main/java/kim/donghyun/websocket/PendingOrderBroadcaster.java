package kim.donghyun.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import kim.donghyun.model.entity.TradeOrder;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PendingOrderBroadcaster {

    private final SimpMessagingTemplate messagingTemplate;

    public void broadcast(TradeOrder order) {
        messagingTemplate.convertAndSend("/topic/pending", order);
    }
}