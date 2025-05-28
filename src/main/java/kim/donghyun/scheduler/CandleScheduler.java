package kim.donghyun.scheduler;

import kim.donghyun.service.CandleAggregator;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CandleScheduler {

    private final CandleAggregator candleAggregator;

    // 매 1분 0초마다 실행 (예: 14:01:00, 14:02:00, ...)
    @Scheduled(cron = "0 * * * * *")
    public void runCandleJob() {
        candleAggregator.generate1MinCandle();
    }

    // ✅ 15분마다 15분봉 생성 (ex: 14:00, 14:15, 14:30, ...)
    @Scheduled(cron = "0 0/15 * * * *")
    public void generate15Min() {
        candleAggregator.generate15MinCandle();
    }

    // ✅ 매 정각마다 1시간봉 생성 (ex: 14:00, 15:00, ...)
    @Scheduled(cron = "0 0 * * * *")
    public void generate1Hour() {
        candleAggregator.generate1HourCandle();
    }

    // ✅ 매일 00:00에 1일봉 생성
    @Scheduled(cron = "0 0 0 * * *")
    public void generate1Day() {
        candleAggregator.generate1DayCandle();
    }

    // ✅ 매주 월요일 00:00에 1주봉 생성
    @Scheduled(cron = "0 0 0 * * MON")
    public void generate1Week() {
        candleAggregator.generate1WeekCandle();
    }

    // ✅ 매월 1일 00:00에 1개월봉 생성
    @Scheduled(cron = "0 0 0 1 * *")
    public void generate1Month() {
        candleAggregator.generate1MonthCandle();
    }
    
}
