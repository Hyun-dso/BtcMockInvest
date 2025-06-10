package kim.donghyun.websocket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kim.donghyun.model.entity.BtcPrice;
import kim.donghyun.repository.BtcPriceRepository;
import kim.donghyun.service.OrderBookService;
import kim.donghyun.util.OrderBookCache;
import kim.donghyun.util.PriceCache;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderBookBroadcaster {

    private final SimpMessagingTemplate messagingTemplate;
    private final OrderBookService orderBookService;
    private final PriceCache priceCache;
    private final BtcPriceRepository btcPriceRepository;
    private final OrderBookCache orderBookCache;

    @Scheduled(fixedDelay = 1000)
    public void broadcastOrderBook() {
        BigDecimal currentPrice = BigDecimal.valueOf(priceCache.getLatestPrice());
//        System.out.println("📡 브로드캐스트 직전 가격: " + currentPrice);

        int depth = 6;
        BigDecimal step = new BigDecimal("0.01");

        Map<String, Object> orderbook = new HashMap<>();
        
        Map<BigDecimal, BigDecimal> asks = new LinkedHashMap<>();
        for (int i = depth; i > 0; i--) {
            BigDecimal priceLevel = currentPrice.add(step.multiply(BigDecimal.valueOf(i))).setScale(2, RoundingMode.HALF_UP);
            BigDecimal lower = priceLevel.subtract(step);
            BigDecimal qty = orderBookService.getPendingAskQuantityInRange(lower, priceLevel);
            asks.put(priceLevel, qty);
        }

        Map<BigDecimal, BigDecimal> bids = new LinkedHashMap<>();
        for (int i = 0; i < depth; i++) {
            BigDecimal priceLevel = currentPrice.subtract(step.multiply(BigDecimal.valueOf(i + 1))).setScale(2, RoundingMode.HALF_UP);
            BigDecimal upper = priceLevel.add(step);
            BigDecimal qty = orderBookService.getPendingBidQuantityInRange(priceLevel, upper);
            bids.put(priceLevel, qty);
        }

        orderBookCache.update(asks, bids);
        
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
