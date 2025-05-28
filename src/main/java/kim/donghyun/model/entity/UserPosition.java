package kim.donghyun.model.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class UserPosition {
    private Long userId;
    private String positionType;      // "LONG", "SHORT", "SPOT" 등 (현재는 SPOT)
    private BigDecimal entryPrice;    // 평균 진입가
    private BigDecimal amount;        // 총 보유 수량
    private BigDecimal currentPrice;  // 현재 시세 (PriceCache에서 주입)
    private BigDecimal pnlPercent;    // 수익률 (%) ← 계산된 값
}
