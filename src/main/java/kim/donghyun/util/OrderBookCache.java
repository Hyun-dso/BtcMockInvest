package kim.donghyun.util;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class OrderBookCache {
    private final Map<BigDecimal, Map<BigDecimal, BigDecimal>> asksByTick = new LinkedHashMap<>();
    private final Map<BigDecimal, Map<BigDecimal, BigDecimal>> bidsByTick = new LinkedHashMap<>();

    public synchronized void update(BigDecimal tick, Map<BigDecimal, BigDecimal> asks, Map<BigDecimal, BigDecimal> bids) {
        asksByTick.put(tick, new LinkedHashMap<>(asks));
        bidsByTick.put(tick, new LinkedHashMap<>(bids));
    }
    
        public synchronized Map<BigDecimal, BigDecimal> getAsks(BigDecimal tick) {
            return new LinkedHashMap<>(asksByTick.getOrDefault(tick, Map.of()));
    }

        public synchronized Map<BigDecimal, BigDecimal> getBids(BigDecimal tick) {
            return new LinkedHashMap<>(bidsByTick.getOrDefault(tick, Map.of()));
    }
}