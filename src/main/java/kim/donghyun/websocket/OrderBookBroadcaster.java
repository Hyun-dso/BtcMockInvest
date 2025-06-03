package kim.donghyun.websocket;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

        BigDecimal tickSize = currentPrice.compareTo(new BigDecimal("100000")) >= 0
                ? new BigDecimal("0.1")
                : new BigDecimal("0.01");
        int depth = 12;

        Map<String, Object> orderbook = new HashMap<>();
        orderbook.put("asks", orderBookService.getAsks(currentPrice, tickSize, depth));
        orderbook.put("bids", orderBookService.getBids(currentPrice, tickSize, depth));
        orderbook.put("price", currentPrice);

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

        messagingTemplate.convertAndSend("/topic/orderbook", orderbook);
        
        messagingTemplate.convertAndSend("/topic/price", currentPrice);
    }
}
