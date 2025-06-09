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
    
    /**
     * 거래 금액 합계를 반환한다.
     * @return price * amount 값, null 값이 있을 경우 0
     */
    public java.math.BigDecimal getTotal() {
        if (price == null || amount == null) {
            return java.math.BigDecimal.ZERO;
        }
        return price.multiply(amount);
    }
}
