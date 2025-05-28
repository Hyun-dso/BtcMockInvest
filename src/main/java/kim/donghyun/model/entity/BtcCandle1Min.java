package kim.donghyun.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BtcCandle1Min {
    private Long id;
    
    private BigDecimal open; // 시작가
    private BigDecimal high; // 최고가
    private BigDecimal low; // 최저가
    private BigDecimal close; // 종가
    private BigDecimal volume; // 거래량(선택)
    
    private LocalDateTime candleTime; // 기준 시각 (예: 2025-05-27 14:03:00)
    
}