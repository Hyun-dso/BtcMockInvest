package kim.donghyun.service.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
import kim.donghyun.util.PriceCache;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FutureOrderProcessor implements OrderExecutionStrategy {

    private final PriceCache priceCache;
    private final TradeOrderRepository orderRepository;
    private final TradeExecutionRepository tradeExecutionRepository;
    private final TradePushService tradePushService;
    private final WalletService walletService;

    @Override
    public TradeOrder execute(Long userId, OrderType type, BigDecimal amount, BigDecimal price, int leverage) {
        if (leverage < 1 || leverage > 100) {
            throw new IllegalArgumentException("레버리지는 1~100배 사이여야 합니다.");
        }

        BigDecimal execPrice = price != null ? price : BigDecimal.valueOf(priceCache.getLatestPrice());
        BigDecimal total = execPrice.multiply(amount);
        BigDecimal margin = total.divide(BigDecimal.valueOf(leverage), 8, RoundingMode.HALF_UP);

        boolean marginOk = walletService.applyFutureMargin(userId, margin);
        if (!marginOk) {
            throw new RuntimeException("마진 부족으로 주문 실패");
        }

        TradeOrder order = new TradeOrder();
        order.setUserId(userId);
        order.setType(type);
        order.setAmount(amount);
        order.setPrice(execPrice);
        order.setTotal(total);
        order.setOrderMode(OrderMode.FUTURE);
        order.setStatus(OrderStatus.FILLED);
        orderRepository.insert(order);

        TradeExecution execution = new TradeExecution();
        if (type == OrderType.BUY) {
            execution.setBuyOrderId(order.getOrderId());
            execution.setSellOrderId(0L);
        } else {
            execution.setBuyOrderId(0L);
            execution.setSellOrderId(order.getOrderId());
        }
        execution.setPrice(execPrice);
        execution.setAmount(amount);
        tradeExecutionRepository.insert(execution);

        tradePushService.broadcastTrade(order);
        return order;
    }
}