package kim.donghyun.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CandleDTO {
    private long time;         // 차트용 UNIX timestamp (UTC 초 단위)
    private String timeLabel;  // 사용자 표시용 (ex. 2025-06-03 14:00)
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    
    // ✅ UTC 기준으로 CandleDTO 만들기 위한 팩토리 메서드
    public static CandleDTO fromUTC(LocalDateTime candleTime, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close) {
        // ⚠️ UTC로 착각하지 말고, KST 기준으로 처리
        ZonedDateTime kstTime = candleTime.atZone(ZoneId.of("Asia/Seoul"));
        long time = kstTime.toEpochSecond(); // 이게 진짜 시간

        String label = kstTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        return new CandleDTO(time, label, open, high, low, close);
    }
    
    public static CandleDTO fromZonedUTC(ZonedDateTime candleTime, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close) {
        long time = candleTime.toEpochSecond(); // 안전한 UTC
        String label = candleTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                                 .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return new CandleDTO(time, label, open, high, low, close);
    }
    
}
