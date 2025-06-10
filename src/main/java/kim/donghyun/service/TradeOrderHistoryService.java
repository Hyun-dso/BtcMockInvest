package kim.donghyun.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kim.donghyun.model.entity.TradeOrder;
import kim.donghyun.repository.TradeOrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TradeOrderHistoryService {

    private static final int DEFAULT_LIMIT = 30;

    private final TradeOrderRepository tradeOrderRepository;

    public List<TradeOrder> getHistory(Long userId, int limit) {
        if (limit <= 0) {
            limit = DEFAULT_LIMIT;
        }
        List<TradeOrder> history = tradeOrderRepository.findByUserId(userId);
        if (history.size() > limit) {
            history = history.subList(0, limit);
        }
        return history;
    }
}