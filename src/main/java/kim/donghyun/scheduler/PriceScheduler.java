package kim.donghyun.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kim.donghyun.service.BtcPriceService;
import kim.donghyun.util.PriceCache;
import kim.donghyun.util.PriceFetcher;
import kim.donghyun.websocket.PriceWebSocketSender;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class PriceScheduler {

    private final PriceFetcher priceFetcher;
    private final BtcPriceService btcPriceService;
    private final PriceWebSocketSender priceWebSocketSender;
    private final PriceCache priceCache;

    private static final Logger log = LoggerFactory.getLogger(PriceScheduler.class);

<<<<<<< Updated upstream
=======
    // 기존 1초 주기를 0.25초로 단축
>>>>>>> Stashed changes
    @Scheduled(fixedDelay = 1000)
    public void fetchAndBroadcast() {
//    	System.out.println("실행");
        try {
            double price = priceFetcher.fetchPrice();
            
            btcPriceService.savePrice(price); // db 저장 + 캐싱
            
            double cachedPrice = priceCache.getLatestPrice(); // ✅ 반드시 캐시된 값 사용
            priceWebSocketSender.broadcast(cachedPrice);

        } catch (Exception e) {
            log.error("가격 수집 및 브로드캐스트 중 오류 발생", e);
        }
    }
}
