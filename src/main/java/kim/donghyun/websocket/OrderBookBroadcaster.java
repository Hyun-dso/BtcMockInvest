package kim.donghyun.websocket;

import kim.donghyun.service.OrderBookService;
import kim.donghyun.util.PriceCache;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrderBookBroadcaster {

    private final SimpMessagingTemplate messagingTemplate;
    private final OrderBookService orderBookService;
    private final PriceCache priceCache;

    // 1초마다 호가창 갱신 및 전송
    @Scheduled(fixedDelay = 1000)
    public void broadcastOrderBook() {
        BigDecimal currentPrice = BigDecimal.valueOf(priceCache.getLatestPrice());
        System.out.println("📡 브로드캐스트 직전 가격: " + currentPrice);

        BigDecimal tickSize = new BigDecimal("10"); // 호가 간격
        int depth = 10; // 위/아래 각각 10단계

        Map<String, Object> orderbook = new HashMap<>();
        orderbook.put("asks", orderBookService.getAsks(currentPrice, tickSize, depth));
        orderbook.put("bids", orderBookService.getBids(currentPrice, tickSize, depth));
        orderbook.put("price", currentPrice);

        messagingTemplate.convertAndSend("/topic/orderbook", orderbook);
    }
}
