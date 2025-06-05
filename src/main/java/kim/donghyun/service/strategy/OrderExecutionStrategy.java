package kim.donghyun.service.strategy;

import java.math.BigDecimal;

import kim.donghyun.model.entity.TradeOrder;
import kim.donghyun.model.enums.OrderType;

public interface OrderExecutionStrategy {
    TradeOrder execute(Long userId, OrderType type, BigDecimal amount, BigDecimal price);
}