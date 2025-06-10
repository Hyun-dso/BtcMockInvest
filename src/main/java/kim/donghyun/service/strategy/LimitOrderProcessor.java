package kim.donghyun.service.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import kim.donghyun.model.entity.TradeExecution;
import kim.donghyun.model.entity.TradeOrder;
import kim.donghyun.model.enums.OrderMode;
import kim.donghyun.model.enums.OrderStatus;
import kim.donghyun.model.enums.OrderType;
import kim.donghyun.repository.TradeExecutionRepository;
import kim.donghyun.repository.TradeOrderRepository;
import kim.donghyun.service.TradePushService;
import kim.donghyun.service.WalletService;
import kim.donghyun.util.PendingOrderCache;
import kim.donghyun.util.PriceCache;
import kim.donghyun.websocket.PendingOrderBroadcaster;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LimitOrderProcessor implements OrderExecutionStrategy {

    private final TradeOrderRepository orderRepository;
    private final TradeExecutionRepository tradeExecutionRepository;
    private final TradePushService tradePushService;
    private final WalletService walletService;
    private final PriceCache priceCache;
    private final PendingOrderBroadcaster pendingOrderBroadcaster;
    private final PendingOrderCache pendingOrderCache;

    @Override
    public TradeOrder execute(Long userId, OrderType type, BigDecimal amount, BigDecimal price) {
    	BigDecimal marketPrice = BigDecimal.valueOf(priceCache.getLatestPrice());

        TradeOrder order = new TradeOrder();
        order.setUserId(userId);
        order.setType(type);
        order.setAmount(amount);
        order.setPrice(price);
        order.setTotal(price.multiply(amount));
        // 지정가 주문임을 명시적으로 저장한다
        order.setOrderMode(OrderMode.LIMIT);
        order.setCreatedAt(java.time.LocalDateTime.now());
        
        if (price.compareTo(marketPrice) == 0) {
            BigDecimal executedAmount = walletService.applyTradeWithCap(userId, price, amount, type.name());
            if (executedAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("잔고 부족으로 주문 실패");
            }
            amount = executedAmount;
            order.setAmount(amount);
            order.setTotal(price.multiply(amount));
            
            order.setStatus(OrderStatus.FILLED);
            orderRepository.insert(order);

            TradeExecution execution = new TradeExecution();
            if (type == OrderType.BUY) {
                execution.setBuyOrderId(order.getOrderId());
                execution.setSellOrderId(null);
            } else {
                execution.setBuyOrderId(null);
                execution.setSellOrderId(order.getOrderId());
            }
            execution.setPrice(price);
            execution.setAmount(amount);
            execution.setCreatedAt(java.time.LocalDateTime.now());
            tradeExecutionRepository.insert(execution);

            tradePushService.broadcastTrade(order);
        } else {
        	order.setStatus(OrderStatus.PENDING);
            orderRepository.insert(order);
            pendingOrderCache.addOrder(order);
            pendingOrderBroadcaster.broadcast(order);
        }
        return order;
    }
}
