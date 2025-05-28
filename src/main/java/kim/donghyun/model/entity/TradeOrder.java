package kim.donghyun.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import kim.donghyun.model.enums.OrderMode;
import kim.donghyun.model.enums.OrderStatus;
import kim.donghyun.model.enums.OrderType;
import lombok.Data;

@Data
public class TradeOrder {
    private Long orderId;
    private Long userId;
    private OrderType type;
    private BigDecimal amount;
    private BigDecimal price;
    private BigDecimal total;
    private OrderMode orderMode;
    private OrderStatus status;
    private LocalDateTime createdAt;
}
