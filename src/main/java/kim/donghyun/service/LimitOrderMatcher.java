package kim.donghyun.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import kim.donghyun.model.entity.TradeExecution;
import kim.donghyun.model.entity.TradeOrder;
import kim.donghyun.model.enums.OrderStatus;
import kim.donghyun.model.enums.OrderType;
import kim.donghyun.repository.TradeExecutionRepository;
import kim.donghyun.repository.TradeOrderRepository;
import kim.donghyun.util.PendingOrderCache;
import kim.donghyun.util.PriceCache;
import kim.donghyun.websocket.PendingOrderBroadcaster;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LimitOrderMatcher {

    private final TradeOrderRepository orderRepository;
    private final TradeExecutionRepository executionRepository;
    private final WalletService walletService;
    private final TradePushService tradePushService;
    private final PriceCache priceCache;
    private final PendingOrderBroadcaster pendingOrderBroadcaster;
    private final PendingOrderCache pendingOrderCache;

    public void match() {
        BigDecimal price = BigDecimal.valueOf(priceCache.getLatestPrice());
        List<TradeOrder> orders = orderRepository.findPendingOrdersByPrice(price);
        for (TradeOrder order : orders) {
            BigDecimal executedAmount = walletService.applyTradeWithCap(order.getUserId(), price, order.getAmount(), order.getType().name());
            if (executedAmount.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            order.setAmount(executedAmount);
            order.setStatus(OrderStatus.FILLED);
            orderRepository.updateStatus(order);

            TradeExecution exe = new TradeExecution();
            if (order.getType() == OrderType.BUY) {
                exe.setBuyOrderId(order.getOrderId());
                exe.setSellOrderId(null);
            } else {
                exe.setBuyOrderId(null);
                exe.setSellOrderId(order.getOrderId());
            }
            exe.setPrice(price);
            exe.setAmount(order.getAmount());
            exe.setCreatedAt(java.time.LocalDateTime.now());
            executionRepository.insert(exe);

            tradePushService.broadcastTrade(order);
            pendingOrderCache.removeOrder(order.getOrderId());
            pendingOrderBroadcaster.broadcast(order);
        }
    }
}