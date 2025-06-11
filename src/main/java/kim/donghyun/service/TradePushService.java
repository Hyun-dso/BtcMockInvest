package kim.donghyun.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import kim.donghyun.model.entity.TradeOrder;
import kim.donghyun.util.TradeHistoryCache;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TradePushService {

    private final SimpMessagingTemplate messagingTemplate;
    private final TradeHistoryCache tradeHistoryCache;

    public void broadcastTrade(TradeOrder order) {
        Map<String, Object> message = new HashMap<>();
        message.put("price", order.getPrice());
        message.put("amount", order.getAmount());
        message.put("type", order.getType()); // BUY or SELL
        message.put("userId", order.getUserId());
        
        if (order.getCreatedAt() != null) {
            java.time.format.DateTimeFormatter fmt =
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            message.put("createdAt", fmt.format(order.getCreatedAt()));
        }

        messagingTemplate.convertAndSend("/topic/trade", message);
        tradeHistoryCache.addTrade(message);
    }
}
