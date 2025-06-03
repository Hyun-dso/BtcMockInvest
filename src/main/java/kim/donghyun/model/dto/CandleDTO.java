package kim.donghyun.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CandleDTO {
    private long time;         // 차트용 UNIX timestamp (UTC 초 단위)
    private String timeLabel;  // 사용자 표시용 (ex. 2025-06-03 14:00)
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
}
