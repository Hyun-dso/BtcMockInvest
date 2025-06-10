package kim.donghyun.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kim.donghyun.model.dto.PriceQuantityDTO;
import kim.donghyun.repository.TradeOrderRepository;

@Service
public class OrderBookService {

    @Autowired
    private TradeOrderRepository tradeOrderRepository;

    // ✅ 매도 지정가 대기 주문 가져오기 (가격 오름차순)
    public Map<BigDecimal, BigDecimal> getPendingAsks(int depth) {
        List<PriceQuantityDTO> asks = tradeOrderRepository.findPendingLimitAsks(depth);
        Map<BigDecimal, BigDecimal> result = new LinkedHashMap<>();
        for (PriceQuantityDTO dto : asks) {
            result.put(dto.getPrice(), dto.getTotalQuantity());
        }
        return result;
    }

    // ✅ 매수 지정가 대기 주문 가져오기 (가격 내림차순)
    public Map<BigDecimal, BigDecimal> getPendingBids(int depth) {
        List<PriceQuantityDTO> bids = tradeOrderRepository.findPendingLimitBids(depth);
        Map<BigDecimal, BigDecimal> result = new LinkedHashMap<>();
        for (PriceQuantityDTO dto : bids) {
            result.put(dto.getPrice(), dto.getTotalQuantity());
        }
        return result;
    }
    
    public BigDecimal getPendingBidQuantity(BigDecimal price) {
        BigDecimal qty = tradeOrderRepository.findPendingBidQuantityAtPrice(price);
        return qty != null ? qty : BigDecimal.ZERO;
    }

    public BigDecimal getPendingAskQuantity(BigDecimal price) {
        BigDecimal qty = tradeOrderRepository.findPendingAskQuantityAtPrice(price);
        return qty != null ? qty : BigDecimal.ZERO;
    }

    public BigDecimal getPendingBidQuantityInRange(BigDecimal low, BigDecimal high) {
        BigDecimal qty = tradeOrderRepository.findPendingBidQuantityInRange(low, high);
        return qty != null ? qty : BigDecimal.ZERO;
    }

    public BigDecimal getPendingAskQuantityInRange(BigDecimal low, BigDecimal high) {
        BigDecimal qty = tradeOrderRepository.findPendingAskQuantityInRange(low, high);
        return qty != null ? qty : BigDecimal.ZERO;
    }

    public Map<BigDecimal, BigDecimal> getGroupedPendingQuantities() {
        List<PriceQuantityDTO> rows = tradeOrderRepository.findPendingQuantityGroupedByPrice();
        Map<BigDecimal, BigDecimal> result = new LinkedHashMap<>();
        for (PriceQuantityDTO dto : rows) {
            result.put(dto.getPrice(), dto.getTotalQuantity());
        }
        return result;
    }

    public Map<String, Map<BigDecimal, BigDecimal>> getOrderBookByTick(Map<BigDecimal, BigDecimal> grouped,
                                                                        BigDecimal tick,
                                                                        BigDecimal currentPrice,
                                                                        int depth) {
        Map<BigDecimal, BigDecimal> askBuckets = new LinkedHashMap<>();
        Map<BigDecimal, BigDecimal> bidBuckets = new LinkedHashMap<>();

        for (Map.Entry<BigDecimal, BigDecimal> entry : grouped.entrySet()) {
            BigDecimal price = entry.getKey();
            BigDecimal qty = entry.getValue();

            BigDecimal askKey = price.divide(tick, 0, RoundingMode.CEILING)
                                     .multiply(tick)
                                     .setScale(2, RoundingMode.HALF_UP);
            askBuckets.merge(askKey, qty, BigDecimal::add);

            BigDecimal bidKey = price.divide(tick, 0, RoundingMode.FLOOR)
                                     .multiply(tick)
                                     .setScale(2, RoundingMode.HALF_UP);
            bidBuckets.merge(bidKey, qty, BigDecimal::add);
        }

        Map<BigDecimal, BigDecimal> asks = new LinkedHashMap<>();
        BigDecimal startAsk = currentPrice.divide(tick, 0, RoundingMode.CEILING)
                                          .multiply(tick);
        for (int i = depth; i > 0; i--) {
            BigDecimal priceLevel = startAsk.add(tick.multiply(BigDecimal.valueOf(i)))
                                            .setScale(2, RoundingMode.HALF_UP);
            BigDecimal qty = askBuckets.getOrDefault(priceLevel, BigDecimal.ZERO);
            asks.put(priceLevel, qty);
        }

        Map<BigDecimal, BigDecimal> bids = new LinkedHashMap<>();
        BigDecimal startBid = currentPrice.divide(tick, 0, RoundingMode.FLOOR)
                                          .multiply(tick);
        for (int i = 0; i < depth; i++) {
            BigDecimal priceLevel = startBid.subtract(tick.multiply(BigDecimal.valueOf(i + 1)))
                                            .setScale(2, RoundingMode.HALF_UP);
            BigDecimal qty = bidBuckets.getOrDefault(priceLevel, BigDecimal.ZERO);
            bids.put(priceLevel, qty);
        }

        Map<String, Map<BigDecimal, BigDecimal>> result = new HashMap<>();
        result.put("asks", asks);
        result.put("bids", bids);
        return result;
    }
}
