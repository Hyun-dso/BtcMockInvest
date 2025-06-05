package kim.donghyun.scheduler;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kim.donghyun.model.entity.FuturePosition;
import kim.donghyun.model.enums.OrderType;
import kim.donghyun.service.FuturePositionService;
import kim.donghyun.util.PriceCache;
import lombok.RequiredArgsConstructor;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class LiquidationScheduler {

    private final FuturePositionService futurePositionService;
    private final PriceCache priceCache;

    @Scheduled(fixedDelay = 1000)
    public void checkLiquidation() {
        List<FuturePosition> positions = futurePositionService.getOpenPositions();
        BigDecimal price = BigDecimal.valueOf(priceCache.getLatestPrice());
        for (FuturePosition p : positions) {
            if (!p.isOpen()) continue;
            if (p.getType() == OrderType.BUY && price.compareTo(p.getLiquidationPrice()) <= 0) {
                futurePositionService.closePosition(p.getId());
            } else if (p.getType() == OrderType.SELL && price.compareTo(p.getLiquidationPrice()) >= 0) {
                futurePositionService.closePosition(p.getId());
            }
        }
    }
}