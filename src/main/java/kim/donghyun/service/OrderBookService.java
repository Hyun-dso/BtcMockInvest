package kim.donghyun.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OrderBookService {

    private static final Random random = new Random();

    // 매도 호가 (현재가 기준 위로)
    public Map<BigDecimal, BigDecimal> getAsks(BigDecimal currentPrice, BigDecimal tickSize, int depth) {
        Map<BigDecimal, BigDecimal> asks = new LinkedHashMap<>();
        for (int i = depth; i > 0; i--) {
            BigDecimal price = currentPrice.add(tickSize.multiply(BigDecimal.valueOf(i))).setScale(2, RoundingMode.HALF_UP);
            BigDecimal quantity = randomQuantity(); // 랜덤 더미 수량
            asks.put(price, quantity);
        }
        return asks;
    }

    // 매수 호가 (현재가 기준 아래로)
    public Map<BigDecimal, BigDecimal> getBids(BigDecimal currentPrice, BigDecimal tickSize, int depth) {
        Map<BigDecimal, BigDecimal> bids = new LinkedHashMap<>();
        for (int i = depth; i > 0; i--) {
            BigDecimal price = currentPrice.subtract(tickSize.multiply(BigDecimal.valueOf(i))).setScale(2, RoundingMode.HALF_UP);
            BigDecimal quantity = randomQuantity(); // 랜덤 더미 수량
            bids.put(price, quantity);
        }
        return bids;
    }

    // 더미 수량 생성기 (0.01 ~ 0.5 BTC)
    private BigDecimal randomQuantity() {
        double amount = 0.01 + (0.49 * random.nextDouble());
        return BigDecimal.valueOf(amount).setScale(4, RoundingMode.HALF_UP);
    }
}
