package kim.donghyun.model.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
    
    /**
     * 포맷팅된 거래 시간 문자열을 반환한다.
     * @return "yyyy-MM-dd HH:mm:ss" 형식의 날짜 문자열, createdAt이 null이면 빈 문자열
     */
    public String getFormattedCreatedAt() {
        if (createdAt == null) {
            return "";
        }
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return createdAt.format(f);
    }

    /**
     * {@link java.util.Date} 형태의 생성 시각을 반환한다.
     * JSTL의 fmt:formatDate에서 사용할 수 있도록 변환한다.
     * @return Date 또는 createdAt이 null이면 null
     */
    public Date getCreatedAtDate() {
        if (createdAt == null) {
            return null;
        }
        return Timestamp.valueOf(createdAt);
    }
}
