package kim.donghyun.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kim.donghyun.model.entity.TradeExecution;
import kim.donghyun.repository.TradeExecutionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TradeHistoryService {

    private static final int DEFAULT_LIMIT = 30;

    private final TradeExecutionRepository tradeExecutionRepository;

    public List<TradeExecution> getHistory(Long userId, int limit) {
        if (limit <= 0) {
            limit = DEFAULT_LIMIT;
        }
        List<TradeExecution> history = tradeExecutionRepository.findByUserId(userId);
        if (history.size() > limit) {
            history = history.subList(0, limit);
        }
        return history;
    }
}