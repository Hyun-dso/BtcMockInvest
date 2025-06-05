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
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LimitOrderProcessor implements OrderExecutionStrategy {

    private final TradeOrderRepository orderRepository;
    private final TradeExecutionRepository tradeExecutionRepository;
    private final TradePushService tradePushService;
    private final WalletService walletService;

    @Override
    public TradeOrder execute(Long userId, OrderType type, BigDecimal amount, BigDecimal price, int leverage) {
        BigDecimal total = price.multiply(amount);

        boolean success = walletService.applyTrade(userId, price, amount, type.name());
        if (!success) {
            throw new RuntimeException("잔고 부족으로 주문 실패");
        }

        TradeOrder order = new TradeOrder();
        order.setUserId(userId);
        order.setType(type);
        order.setAmount(amount);
        order.setPrice(price);
        order.setTotal(total);
        order.setOrderMode(OrderMode.LIMIT);
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
        tradeExecutionRepository.insert(execution);

        tradePushService.broadcastTrade(order);
        return order;
    }
}