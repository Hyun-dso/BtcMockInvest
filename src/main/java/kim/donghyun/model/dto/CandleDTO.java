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
        // 🎯 LocalDateTime이 UTC 기준임을 가정하고, 정확하게 Epoch 초로 변환
        long time = candleTime.atOffset(ZoneOffset.UTC).toEpochSecond();

        // 🕒 사용자 표시용: KST로 변환하여 라벨 처리
        String label = candleTime
                .atOffset(ZoneOffset.UTC)                       // UTC로 고정
                .atZoneSameInstant(ZoneId.of("Asia/Seoul"))     // 한국 시간대 적용
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        return new CandleDTO(time, label, open, high, low, close);
    }
    
    public static CandleDTO fromZonedUTC(ZonedDateTime candleTime, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close) {
        long time = candleTime.toEpochSecond(); // 안전한 UTC
        String label = candleTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                                 .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return new CandleDTO(time, label, open, high, low, close);
    }
    
}
