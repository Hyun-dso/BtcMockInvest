package kim.donghyun.websocket;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kim.donghyun.model.entity.BtcPrice;
import kim.donghyun.repository.BtcPriceRepository;
import kim.donghyun.service.OrderBookService;
import kim.donghyun.util.PriceCache;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderBookBroadcaster {

    private final SimpMessagingTemplate messagingTemplate;
    private final OrderBookService orderBookService;
    private final PriceCache priceCache;
    private final BtcPriceRepository btcPriceRepository;

    @Scheduled(fixedDelay = 1000)
    public void broadcastOrderBook() {
        BigDecimal currentPrice = BigDecimal.valueOf(priceCache.getLatestPrice());
//        System.out.println("📡 브로드캐스트 직전 가격: " + currentPrice);

        int depth = 6;

        Map<String, Object> orderbook = new HashMap<>();
 
        Map<BigDecimal, BigDecimal> asks = orderBookService.getPendingAsks(depth);
        Map<BigDecimal, BigDecimal> bids = orderBookService.getPendingBids(depth);
        
        orderbook.put("asks", asks);
        orderbook.put("bids", bids);
        orderbook.put("price", currentPrice);
        
//        System.out.println("📡 호가창 데이터 - 매도 (asks): " + orderbook.get("asks"));
//        System.out.println("📡 호가창 데이터 - 매수 (bids): " + orderbook.get("bids"));

        // ✅ 종가 조회: 어제의 UTC 기준 (KST 기준 09:00)
        LocalDateTime referenceTime = LocalDateTime.now().withHour(9).withMinute(0).withSecond(0).withNano(0);
        BtcPrice prevClose = btcPriceRepository.findClosestAfter(referenceTime.minusDays(1));

        if (prevClose != null) {
            orderbook.put("prevClose", prevClose.getPrice());

            // ✅ Timestamp → Date 변환
            Date createdAtDate = new Date(prevClose.getCreatedAt().getTime());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            orderbook.put("prevCloseTime", sdf.format(createdAtDate));
        }

        // 전송
        messagingTemplate.convertAndSend("/topic/orderbook", orderbook);
        

        // ✅ 실시간 가격 + 시간 전송 (초 단위 timestamp)
        Map<String, Object> pricePayload = new HashMap<>();
        pricePayload.put("price", currentPrice);
        // pricePayload.put("timestamp", System.currentTimeMillis() / 1000);
        pricePayload.put("timestamp", Instant.now().getEpochSecond()); // ✅ 정확

        messagingTemplate.convertAndSend("/topic/price", pricePayload);
    }
}
