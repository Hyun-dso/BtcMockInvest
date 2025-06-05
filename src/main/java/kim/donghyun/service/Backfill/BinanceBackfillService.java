package kim.donghyun.service.Backfill;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import kim.donghyun.model.entity.BtcCandle15Min;
import kim.donghyun.model.entity.BtcCandle1D;
import kim.donghyun.model.entity.BtcCandle1H;
import kim.donghyun.model.entity.BtcCandle1M;
import kim.donghyun.model.entity.BtcCandle1Min;
import kim.donghyun.model.entity.BtcCandle1W;
import kim.donghyun.repository.BtcCandle15MinRepository;
import kim.donghyun.repository.BtcCandle1DRepository;
import kim.donghyun.repository.BtcCandle1HRepository;
import kim.donghyun.repository.BtcCandle1MRepository;
import kim.donghyun.repository.BtcCandle1MinRepository;
import kim.donghyun.repository.BtcCandle1WRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BinanceBackfillService {
    private static final String BASE_URL = "https://api.binance.com/api/v3/klines";
    private static final String SYMBOL = "BTCUSDT";
    private static final long REQUEST_INTERVAL_MS = 300L;
    private static final long TWO_YEARS_MS = 365L * 2 * 24 * 60 * 60 * 1000;

    private final BtcCandle1MinRepository candle1MinRepo;
    private final BtcCandle15MinRepository candle15MinRepo;
    private final BtcCandle1HRepository candle1HRepo;
    private final BtcCandle1DRepository candle1DRepo;
    private final BtcCandle1WRepository candle1WRepo;
    private final BtcCandle1MRepository candle1MRepo;

    private final RestTemplate restTemplate = new RestTemplate();

    public void backfill() throws InterruptedException {
        long end = System.currentTimeMillis();
        long start = end - TWO_YEARS_MS;

        fetchInterval("1m", start, end);
        fetchInterval("15m", start, end);
        fetchInterval("1h", start, end);
        fetchInterval("1d", start, end);
        fetchInterval("1w", start, end);
        fetchInterval("1M", start, end);
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
                LocalDateTime candleTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(openTime), ZoneOffset.UTC);
                switch (interval) {
                    case "1m" -> {
                        BtcCandle1Min c = new BtcCandle1Min();
                        c.setOpen(open); c.setHigh(high); c.setLow(low); c.setClose(close);
                        c.setVolume(volume); c.setCandleTime(candleTime);
                        candle1MinRepo.insertCandle(c);
                    }
                    case "15m" -> {
                        BtcCandle15Min c = new BtcCandle15Min();
                        c.setOpen(open); c.setHigh(high); c.setLow(low); c.setClose(close);
                        c.setVolume(volume); c.setCandleTime(candleTime);
                        candle15MinRepo.insertCandle(c);
                    }
                    case "1h" -> {
                        BtcCandle1H c = new BtcCandle1H();
                        c.setOpen(open); c.setHigh(high); c.setLow(low); c.setClose(close);
                        c.setVolume(volume); c.setCandleTime(candleTime);
                        candle1HRepo.insertCandle(c);
                    }
                    case "1d" -> {
                        BtcCandle1D c = new BtcCandle1D();
                        c.setOpen(open); c.setHigh(high); c.setLow(low); c.setClose(close);
                        c.setVolume(volume); c.setCandleTime(candleTime);
                        candle1DRepo.insertCandle(c);
                    }
                    case "1w" -> {
                        BtcCandle1W c = new BtcCandle1W();
                        c.setOpen(open); c.setHigh(high); c.setLow(low); c.setClose(close);
                        c.setVolume(volume); c.setCandleTime(candleTime);
                        candle1WRepo.insertCandle(c);
                    }
                    case "1M" -> {
                        BtcCandle1M c = new BtcCandle1M();
                        c.setOpen(open); c.setHigh(high); c.setLow(low); c.setClose(close);
                        c.setVolume(volume); c.setCandleTime(candleTime);
                        candle1MRepo.insertCandle(c);
                    }
                }
            }

            long lastClose = ((Number) res.get(res.size() - 1).get(6)).longValue();
            cursor = lastClose + 1;
            Thread.sleep(REQUEST_INTERVAL_MS);
        }
    }
}