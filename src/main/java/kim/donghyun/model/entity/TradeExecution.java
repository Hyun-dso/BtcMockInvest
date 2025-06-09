package kim.donghyun.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TradeExecution {
    private Long id;
    private Long buyOrderId;
    private Long sellOrderId;
    private BigDecimal price;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    
    // 사용자가 매수했는지 매도했는지 구분하기 위한 필드
    private String userType;
}
