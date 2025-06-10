package kim.donghyun.util;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class OrderBookCache {
    private final Map<BigDecimal, BigDecimal> asks = new LinkedHashMap<>();
    private final Map<BigDecimal, BigDecimal> bids = new LinkedHashMap<>();

    public synchronized void update(Map<BigDecimal, BigDecimal> newAsks, Map<BigDecimal, BigDecimal> newBids) {
        asks.clear();
        asks.putAll(newAsks);
        bids.clear();
        bids.putAll(newBids);
    }

    public synchronized Map<BigDecimal, BigDecimal> getAsks() {
        return new LinkedHashMap<>(asks);
    }

    public synchronized Map<BigDecimal, BigDecimal> getBids() {
        return new LinkedHashMap<>(bids);
    }
}