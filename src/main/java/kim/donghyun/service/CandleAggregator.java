package kim.donghyun.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kim.donghyun.model.dto.CandleDTO;
import kim.donghyun.model.entity.BtcCandle15Min;
import kim.donghyun.model.entity.BtcCandle1D;
import kim.donghyun.model.entity.BtcCandle1H;
import kim.donghyun.model.entity.BtcCandle1M;
import kim.donghyun.model.entity.BtcCandle1Min;
import kim.donghyun.model.entity.BtcCandle1W;
import kim.donghyun.model.entity.BtcPrice;
import kim.donghyun.repository.BtcCandle15MinRepository;
import kim.donghyun.repository.BtcCandle1DRepository;
import kim.donghyun.repository.BtcCandle1HRepository;
import kim.donghyun.repository.BtcCandle1MRepository;
import kim.donghyun.repository.BtcCandle1MinRepository;
import kim.donghyun.repository.BtcCandle1WRepository;
import kim.donghyun.repository.BtcPriceRepository;
import kim.donghyun.websocket.CandleBroadcaster;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CandleAggregator {

    private final BtcPriceRepository btcPriceRepository;
    private final BtcCandle1MinRepository btcCandle1MinRepository;
    private final BtcCandle15MinRepository btcCandle15MinRepository;
    private final BtcCandle1HRepository btcCandle1HRepository;
    private final BtcCandle1DRepository btcCandle1DRepository;
    private final BtcCandle1WRepository btcCandle1WRepository;
    private final BtcCandle1MRepository btcCandle1MRepository;
    private final CandleBroadcaster candleBroadcaster;

    public void generate1MinCandle() {
        // 현재 시간에서 가장 가까운 지난 분 단위로 기준 시간 생성
        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC).withSecond(0).withNano(0);
        ZonedDateTime oneMinuteAgoUtc = nowUtc.minusMinutes(1);
        LocalDateTime oneMinuteAgo = oneMinuteAgoUtc.toLocalDateTime(); // ✅ UTC 기준 LocalDateTime
        LocalDateTime now = nowUtc.toLocalDateTime(); // 현재 시각도 UTC 기준으로 조회

        // 1분간의 가격 데이터 조회
        List<BtcPrice> prices = btcPriceRepository.findPricesBetween(oneMinuteAgo, now);

        if (prices == null || prices.isEmpty()) return;

        List<BigDecimal> priceList = prices.stream()
                .map(p -> BigDecimal.valueOf(p.getPrice().doubleValue()))
                .collect(Collectors.toList());

        BigDecimal open = priceList.get(0);
        BigDecimal high = priceList.stream().max(BigDecimal::compareTo).get();
        BigDecimal low = priceList.stream().min(BigDecimal::compareTo).get();
        BigDecimal close = priceList.get(priceList.size() - 1);
        BigDecimal volume = BigDecimal.ZERO; // 추후 거래량 집계 시 사용

        BtcCandle1Min candle = new BtcCandle1Min();
        candle.setOpen(open);
        candle.setHigh(high);
        candle.setLow(low);
        candle.setClose(close);
        candle.setVolume(volume);
        candle.setCandleTime(oneMinuteAgo); // 기준 시각은 1분 전

        btcCandle1MinRepository.insertCandle(candle);
        
        CandleDTO dto = CandleDTO.fromUTC(oneMinuteAgo, open, high, low, close);
        
        candleBroadcaster.broadcastCandle("1m", dto);

    }
    
    public void generate15MinCandle() {
        // 현재 시간에서 가장 가까운 지난 15분 단위로 기준 시간 생성
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        int minute = now.getMinute();
        int mod = minute % 15;
        LocalDateTime fifteenMinAgo = now.minusMinutes(mod).minusMinutes(15); // 정각 15분 전 기준

        LocalDateTime end = fifteenMinAgo.plusMinutes(15);

        // 15분간의 1분봉 조회
        List<BtcCandle1Min> candles = btcCandle1MinRepository.findByTimeRange(fifteenMinAgo, end);

        if (candles == null || candles.isEmpty()) return;

        List<BigDecimal> openCloseList = candles.stream()
                .map(c -> List.of(c.getOpen(), c.getClose()))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        BigDecimal open = candles.get(0).getOpen();
        BigDecimal close = candles.get(candles.size() - 1).getClose();
        BigDecimal high = candles.stream().map(BtcCandle1Min::getHigh).max(BigDecimal::compareTo).orElse(open);
        BigDecimal low = candles.stream().map(BtcCandle1Min::getLow).min(BigDecimal::compareTo).orElse(open);
        BigDecimal volume = BigDecimal.ZERO;

        BtcCandle15Min candle = new BtcCandle15Min();
        candle.setOpen(open);
        candle.setHigh(high);
        candle.setLow(low);
        candle.setClose(close);
        candle.setVolume(volume);
        candle.setCandleTime(fifteenMinAgo);

        btcCandle15MinRepository.insertCandle(candle);

        CandleDTO dto = CandleDTO.fromUTC(fifteenMinAgo, open, high, low, close);

        candleBroadcaster.broadcastCandle("15m", dto);
    }

    public void generate1HourCandle() {
        // 기준 시각: 현재 시각에서 가장 가까운 지난 정각
        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        LocalDateTime oneHourAgo = now.minusHours(1);

        // 1시간 간격의 1분봉 조회
        List<BtcCandle1Min> candles = btcCandle1MinRepository.findByTimeRange(oneHourAgo, now);

        if (candles == null || candles.isEmpty()) return;

        BigDecimal open = candles.get(0).getOpen();
        BigDecimal close = candles.get(candles.size() - 1).getClose();
        BigDecimal high = candles.stream().map(BtcCandle1Min::getHigh).max(BigDecimal::compareTo).orElse(open);
        BigDecimal low = candles.stream().map(BtcCandle1Min::getLow).min(BigDecimal::compareTo).orElse(open);
        BigDecimal volume = BigDecimal.ZERO; // 추후 확장용

        BtcCandle1H candle = new BtcCandle1H();
        candle.setOpen(open);
        candle.setHigh(high);
        candle.setLow(low);
        candle.setClose(close);
        candle.setVolume(volume);
        candle.setCandleTime(oneHourAgo);

        btcCandle1HRepository.insertCandle(candle);

        CandleDTO dto = CandleDTO.fromUTC(oneHourAgo, open, high, low, close);
        candleBroadcaster.broadcastCandle("1h", dto);
    }

    public void generate1DayCandle() {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime yesterday = today.minusDays(1);

        List<BtcCandle1H> candles = btcCandle1HRepository.findRecentCandles(24);
        if (candles == null || candles.size() < 24) return;

        BigDecimal open = candles.get(0).getOpen();
        BigDecimal high = candles.stream().map(BtcCandle1H::getHigh).max(BigDecimal::compareTo).orElse(open);
        BigDecimal low  = candles.stream().map(BtcCandle1H::getLow).min(BigDecimal::compareTo).orElse(open);
        BigDecimal close = candles.get(candles.size() - 1).getClose();
        BigDecimal volume = candles.stream().map(BtcCandle1H::getVolume)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

        BtcCandle1D candle = new BtcCandle1D();
        candle.setOpen(open);
        candle.setHigh(high);
        candle.setLow(low);
        candle.setClose(close);
        candle.setVolume(volume);
        candle.setCandleTime(yesterday);

        btcCandle1DRepository.insertCandle(candle);
    }

    public void generate1WeekCandle() {
        LocalDateTime thisWeek = LocalDateTime.now().with(java.time.DayOfWeek.MONDAY)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime lastWeek = thisWeek.minusWeeks(1);

        List<BtcCandle1D> candles = btcCandle1DRepository.findRecentCandles(7);
        if (candles == null || candles.size() < 7) return;

        BigDecimal open = candles.get(0).getOpen();
        BigDecimal high = candles.stream().map(BtcCandle1D::getHigh).max(BigDecimal::compareTo).orElse(open);
        BigDecimal low  = candles.stream().map(BtcCandle1D::getLow).min(BigDecimal::compareTo).orElse(open);
        BigDecimal close = candles.get(candles.size() - 1).getClose();
        BigDecimal volume = candles.stream().map(BtcCandle1D::getVolume)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

        BtcCandle1W candle = new BtcCandle1W();
        candle.setOpen(open);
        candle.setHigh(high);
        candle.setLow(low);
        candle.setClose(close);
        candle.setVolume(volume);
        candle.setCandleTime(lastWeek);

        btcCandle1WRepository.insertCandle(candle);
    }

    public void generate1MonthCandle() {
        LocalDateTime thisMonthStart = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime lastMonthStart = thisMonthStart.minusMonths(1);
        LocalDateTime lastMonthEnd = thisMonthStart;

        List<BtcCandle1D> candles = btcCandle1DRepository.findCandlesBetween(lastMonthStart, lastMonthEnd);
        if (candles == null || candles.isEmpty()) return;

        BigDecimal open = candles.get(0).getOpen();
        BigDecimal high = candles.stream().map(BtcCandle1D::getHigh).max(BigDecimal::compareTo).orElse(open);
        BigDecimal low  = candles.stream().map(BtcCandle1D::getLow).min(BigDecimal::compareTo).orElse(open);
        BigDecimal close = candles.get(candles.size() - 1).getClose();
        BigDecimal volume = candles.stream()
                .map(BtcCandle1D::getVolume)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BtcCandle1M candle = new BtcCandle1M();
        candle.setOpen(open);
        candle.setHigh(high);
        candle.setLow(low);
        candle.setClose(close);
        candle.setVolume(volume);
        candle.setCandleTime(lastMonthStart); // 기준 시각 = 지난달 1일 00:00

        btcCandle1MRepository.insertCandle(candle);
    }

    
//    public void generate1MonthCandle() {
//        LocalDateTime thisMonth = LocalDateTime.now().withDayOfMonth(1)
//                .withHour(0).withMinute(0).withSecond(0).withNano(0);
//        LocalDateTime lastMonth = thisMonth.minusMonths(1);
//
//        List<BtcCandle1D> candles = btcCandle1DRepository.findRecentCandles(30);
//        if (candles == null || candles.size() < 30) return;
//
//        BigDecimal open = candles.get(0).getOpen();
//        BigDecimal high = candles.stream().map(BtcCandle1D::getHigh).max(BigDecimal::compareTo).orElse(open);
//        BigDecimal low  = candles.stream().map(BtcCandle1D::getLow).min(BigDecimal::compareTo).orElse(open);
//        BigDecimal close = candles.get(candles.size() - 1).getClose();
//        BigDecimal volume = candles.stream().map(BtcCandle1D::getVolume)
//                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        BtcCandle1M candle = new BtcCandle1M();
//        candle.setOpen(open);
//        candle.setHigh(high);
//        candle.setLow(low);
//        candle.setClose(close);
//        candle.setVolume(volume);
//        candle.setCandleTime(lastMonth);
//
//        btcCandle1MRepository.insertCandle(candle);
//    }

}
