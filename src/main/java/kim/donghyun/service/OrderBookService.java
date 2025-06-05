package kim.donghyun.service;

import java.math.BigDecimal;
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
}
