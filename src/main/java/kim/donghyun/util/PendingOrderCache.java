package kim.donghyun.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.springframework.stereotype.Component;

import kim.donghyun.model.entity.TradeOrder;

@Component
public class PendingOrderCache {
    private static final int MAX_SIZE = 20;
    private final Deque<TradeOrder> recentOrders = new ArrayDeque<>();

    public synchronized void addOrder(TradeOrder order) {
        recentOrders.addFirst(order);
        if (recentOrders.size() > MAX_SIZE) {
            recentOrders.removeLast();
        }
    }

    public synchronized void removeOrder(Long orderId) {
        recentOrders.removeIf(o -> orderId.equals(o.getOrderId()));
    }

    public synchronized List<TradeOrder> getRecentOrders() {
        return new ArrayList<>(recentOrders);
    }
}