package kim.donghyun.model.entity;

import java.math.BigDecimal;

import kim.donghyun.model.enums.OrderType;
import lombok.Data;

@Data
public class FuturePosition {
    private Long id;
    private Long userId;
    private OrderType type; // BUY -> Long, SELL -> Short
    private BigDecimal amount;
    private BigDecimal entryPrice;
    private int leverage;
    private BigDecimal liquidationPrice;
    private boolean open;
}