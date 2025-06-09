package kim.donghyun.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class TradeHistoryCache {
    // Limit the history to the latest 10 trades
    private static final int MAX_SIZE = 10;
    private final Deque<Map<String, Object>> recentTrades = new ArrayDeque<>();

    public synchronized void addTrade(Map<String, Object> trade) {
        recentTrades.addFirst(trade);
        if (recentTrades.size() > MAX_SIZE) {
            recentTrades.removeLast();
        }
    }

    public synchronized List<Map<String, Object>> getRecentTrades() {
        return new ArrayList<>(recentTrades);
    }
}