package kim.donghyun.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kim.donghyun.service.BtcPriceService;
import kim.donghyun.util.PriceFetcher;
import kim.donghyun.websocket.PriceWebSocketHandler;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PriceScheduler {

    private final PriceFetcher priceFetcher;
    private final BtcPriceService btcPriceService;
    private final PriceWebSocketHandler priceWebSocketHandler; // 추가

    private static final Logger log = LoggerFactory.getLogger(PriceScheduler.class);

    @Scheduled(fixedDelay = 1000)
    public void fetchAndSave() {
        try {
            double price = priceFetcher.fetchPrice();
            btcPriceService.savePrice(price);
            priceWebSocketHandler.broadcast(String.valueOf(price)); // 실시간 전송
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
