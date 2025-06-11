package kim.donghyun.service.Backfill;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import kim.donghyun.model.entity.BtcCandle1Min;
import kim.donghyun.repository.BtcCandle1MinRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BinanceBackfillService {
    private static final String BASE_URL = "https://api.binance.com/api/v3/klines";
    private static final String SYMBOL = "BTCUSDT";
    private static final long REQUEST_INTERVAL_MS = 300L;
    private static final long TWO_YEARS_MS = 365L * 5 * 24 * 60 * 60 * 1000;
    // 5년
    // private static final long FIVE_YEARS_MS = 365L * 5 * 24 * 60 * 60 * 1000;

    private final BtcCandle1MinRepository candle1MinRepo;

    private final RestTemplate restTemplate = new RestTemplate();

    public void backfill() throws InterruptedException {
        long end = System.currentTimeMillis();
        long start = end - TWO_YEARS_MS;

        fetchInterval("1m", start, end);
    }

    private void fetchInterval(String interval, long start, long end) throws InterruptedException {
        long cursor = start;
        while (cursor < end) {
            String url = BASE_URL + "?symbol=" + SYMBOL +
                    "&interval=" + interval +
                    "&startTime=" + cursor +
                    "&limit=1000";

            List<List<Object>> res = restTemplate.getForObject(url, List.class);
            if (res == null || res.isEmpty()) break;

            for (List<Object> e : res) {
                long openTime = ((Number) e.get(0)).longValue();
                BigDecimal open = new BigDecimal((String) e.get(1));
                BigDecimal high = new BigDecimal((String) e.get(2));
                BigDecimal low = new BigDecimal((String) e.get(3));
                BigDecimal close = new BigDecimal((String) e.get(4));
                BigDecimal volume = new BigDecimal((String) e.get(5));
                
                LocalDateTime candleTime = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(openTime),
                        java.time.ZoneId.of("Asia/Seoul"));
                BtcCandle1Min c = new BtcCandle1Min();
                c.setOpen(open);
                c.setHigh(high);
                c.setLow(low);
                c.setClose(close);
                c.setVolume(volume);
                c.setCandleTime(candleTime);
                candle1MinRepo.insertCandle(c);
            }

            long lastClose = ((Number) res.get(res.size() - 1).get(6)).longValue();
            cursor = lastClose + 1;
            Thread.sleep(REQUEST_INTERVAL_MS);
        }
    }
}