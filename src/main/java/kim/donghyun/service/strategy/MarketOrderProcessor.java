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
import kim.donghyun.service.OrderBookService;
import kim.donghyun.service.TradePushService;
import kim.donghyun.service.WalletService;
import kim.donghyun.util.PriceCache;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MarketOrderProcessor implements OrderExecutionStrategy {

    private final PriceCache priceCache;
    private final OrderBookService orderBookService;
    private final TradeOrderRepository orderRepository;
    private final TradePushService tradePushService;
    private final TradeExecutionRepository tradeExecutionRepository;
    private final WalletService walletService;

    @Override
    public TradeOrder execute(Long userId, OrderType type, BigDecimal amount, BigDecimal price) {
        BigDecimal execPrice = BigDecimal.valueOf(priceCache.getLatestPrice());

        
        if (price != null && price.compareTo(execPrice) != 0) {
            throw new IllegalArgumentException("시장가 주문은 현재 가격으로만 체결됩니다.");
        }
        
        if (type == OrderType.BUY) {
            execPrice = orderBookService.getPendingAsks(1)
                                       .keySet()
                                       .stream()
                                       .findFirst()
                                       .orElse(execPrice);
        } else {
            execPrice = orderBookService.getPendingBids(1)
                                       .keySet()
                                       .stream()
                                       .findFirst()
                                       .orElse(execPrice);
        }

        BigDecimal executedAmount = walletService.applyTradeWithCap(userId, execPrice, amount, type.name());
        if (executedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("잔고 부족으로 주문 실패");
        }
        amount = executedAmount;
        BigDecimal total = execPrice.multiply(amount);
        
        TradeOrder order = new TradeOrder();
        order.setUserId(userId);
        order.setType(type);
        order.setAmount(amount);
        order.setPrice(execPrice);
        order.setTotal(total);
        order.setCreatedAt(java.time.LocalDateTime.now());
        order.setOrderMode(OrderMode.MARKET);
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
        execution.setPrice(execPrice);
        execution.setAmount(amount);
        execution.setCreatedAt(java.time.LocalDateTime.now());
        tradeExecutionRepository.insert(execution);

        tradePushService.broadcastTrade(order);
        return order;
    }
}