package kim.donghyun.model.entity;

import kim.donghyun.model.enums.OrderType;
import kim.donghyun.model.enums.OrderMode;
import kim.donghyun.model.enums.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;

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
    private String createdAt;
}
