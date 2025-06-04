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

        // ✅ 디버깅: ZonedDateTime 값과 Epoch 확인
        System.out.println("✅ [DEBUG] 정식봉 기준 시간 (ZonedDateTime): " + oneMinuteAgoUtc);
        System.out.println("🧠 [DEBUG] toEpochSecond(): " + oneMinuteAgoUtc.toEpochSecond());

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
        BigDecimal volume = BigDecimal.ZERO;

        BtcCandle1Min candle = new BtcCandle1Min();
        candle.setOpen(open);
        candle.setHigh(high);
        candle.setLow(low);
        candle.setClose(close);
        candle.setVolume(volume);
        candle.setCandleTime(oneMinuteAgo); // 기준 시각은 1분 전

        btcCandle1MinRepository.insertCandle(candle);

        CandleDTO dto = CandleDTO.fromZonedUTC(oneMinuteAgoUtc, open, high, low, close);
        candleBroadcaster.broadcastCandle("1m", dto);
    }
    
    public void generate15MinCandle() {
        // ⏱️ UTC 기준 현재 시간 정리 (초/나노 제거)
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC).withSecond(0).withNano(0);

        // 📌 기준 시각: 가장 가까운 지난 정각 15분
        int minute = now.getMinute();
        ZonedDateTime start = now.minusMinutes(minute % 15).minusMinutes(15);
        ZonedDateTime end = start.plusMinutes(15);

        // 🔍 15분 구간의 1분봉 조회
        List<BtcCandle1Min> candles = btcCandle1MinRepository.findByTimeRange(
            start.toLocalDateTime(),
            end.toLocalDateTime()
        );

        if (candles == null || candles.isEmpty()) return;

        // 📊 시가/고가/저가/종가/거래량 계산
        BigDecimal open = candles.get(0).getOpen();
        BigDecimal close = candles.get(candles.size() - 1).getClose();
        BigDecimal high = candles.stream().map(BtcCandle1Min::getHigh).max(BigDecimal::compareTo).orElse(open);
        BigDecimal low  = candles.stream().map(BtcCandle1Min::getLow).min(BigDecimal::compareTo).orElse(open);
        BigDecimal volume = BigDecimal.ZERO; // 거래량 추후 구현 예정

        // 🧾 DB 저장용 엔티티 생성
        BtcCandle15Min candle = new BtcCandle15Min();
        candle.setOpen(open);
        candle.setHigh(high);
        candle.setLow(low);
        candle.setClose(close);
        candle.setVolume(volume);
        candle.setCandleTime(start.toLocalDateTime());

        btcCandle15MinRepository.insertCandle(candle);

        // 📤 클라이언트 전송용 DTO 생성 (UTC 기반)
        CandleDTO dto = CandleDTO.fromZonedUTC(start, open, high, low, close);
        candleBroadcaster.broadcastCandle("15m", dto);
    }

    public void generate1HourCandle() {
        // ⏱️ 현재 시간에서 가장 가까운 지난 정각 (UTC)
        ZonedDateTime end = ZonedDateTime.now(ZoneOffset.UTC).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime start = end.minusHours(1);

        // 🔍 1시간 구간의 1분봉 조회
        List<BtcCandle1Min> candles = btcCandle1MinRepository.findByTimeRange(
            start.toLocalDateTime(),
            end.toLocalDateTime()
        );

        if (candles == null || candles.isEmpty()) return;

        // 📊 시가/고가/저가/종가/거래량 계산
        BigDecimal open = candles.get(0).getOpen();
        BigDecimal close = candles.get(candles.size() - 1).getClose();
        BigDecimal high = candles.stream().map(BtcCandle1Min::getHigh).max(BigDecimal::compareTo).orElse(open);
        BigDecimal low  = candles.stream().map(BtcCandle1Min::getLow).min(BigDecimal::compareTo).orElse(open);
        BigDecimal volume = BigDecimal.ZERO;

        // 🧾 DB 저장용 엔티티 생성
        BtcCandle1H candle = new BtcCandle1H();
        candle.setOpen(open);
        candle.setHigh(high);
        candle.setLow(low);
        candle.setClose(close);
        candle.setVolume(volume);
        candle.setCandleTime(start.toLocalDateTime());

        btcCandle1HRepository.insertCandle(candle);

        // 📤 클라이언트 전송용 DTO 생성 (UTC 기준)
        CandleDTO dto = CandleDTO.fromZonedUTC(start, open, high, low, close);
        candleBroadcaster.broadcastCandle("1h", dto);
    }

    public void generate1DayCandle() {
        // ⏱️ 오늘 자정 (UTC 기준)
        ZonedDateTime today = ZonedDateTime.now(ZoneOffset.UTC)
                                           .withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime yesterday = today.minusDays(1);

        // 🔍 어제 하루 동안의 1시간봉 24개 가져오기
        List<BtcCandle1H> candles = btcCandle1HRepository.findRecentCandles(24);
        if (candles == null || candles.size() < 24) return;

        // 📊 시가/고가/저가/종가/거래량 계산
        BigDecimal open = candles.get(0).getOpen();
        BigDecimal close = candles.get(candles.size() - 1).getClose();
        BigDecimal high = candles.stream().map(BtcCandle1H::getHigh).max(BigDecimal::compareTo).orElse(open);
        BigDecimal low  = candles.stream().map(BtcCandle1H::getLow).min(BigDecimal::compareTo).orElse(open);
        BigDecimal volume = candles.stream().map(BtcCandle1H::getVolume).reduce(BigDecimal.ZERO, BigDecimal::add);

        // 🧾 DB 저장
        BtcCandle1D candle = new BtcCandle1D();
        candle.setOpen(open);
        candle.setHigh(high);
        candle.setLow(low);
        candle.setClose(close);
        candle.setVolume(volume);
        candle.setCandleTime(yesterday.toLocalDateTime());

        btcCandle1DRepository.insertCandle(candle);

        // 📤 WebSocket 전송 (원한다면 아래 주석 해제)
        // CandleDTO dto = CandleDTO.fromZonedUTC(yesterday, open, high, low, close);
        // candleBroadcaster.broadcastCandle("1d", dto);
    }

    public void generate1WeekCandle() {
        // ⏱️ 이번 주 월요일 00:00 (UTC)
        ZonedDateTime thisWeek = ZonedDateTime.now(ZoneOffset.UTC)
            .with(java.time.DayOfWeek.MONDAY)
            .withHour(0).withMinute(0).withSecond(0).withNano(0);

        ZonedDateTime lastWeek = thisWeek.minusWeeks(1);

        // 🔍 지난 1주일간의 일봉 7개
        List<BtcCandle1D> candles = btcCandle1DRepository.findRecentCandles(7);
        if (candles == null || candles.size() < 7) return;

        // 📊 시가/고가/저가/종가/거래량 계산
        BigDecimal open = candles.get(0).getOpen();
        BigDecimal close = candles.get(candles.size() - 1).getClose();
        BigDecimal high = candles.stream().map(BtcCandle1D::getHigh).max(BigDecimal::compareTo).orElse(open);
        BigDecimal low  = candles.stream().map(BtcCandle1D::getLow).min(BigDecimal::compareTo).orElse(open);
        BigDecimal volume = candles.stream().map(BtcCandle1D::getVolume).reduce(BigDecimal.ZERO, BigDecimal::add);

        // 🧾 DB 저장
        BtcCandle1W candle = new BtcCandle1W();
        candle.setOpen(open);
        candle.setHigh(high);
        candle.setLow(low);
        candle.setClose(close);
        candle.setVolume(volume);
        candle.setCandleTime(lastWeek.toLocalDateTime());

        btcCandle1WRepository.insertCandle(candle);

        // 📤 WebSocket 전송 (원할 경우 주석 해제)
        // CandleDTO dto = CandleDTO.fromZonedUTC(lastWeek, open, high, low, close);
        // candleBroadcaster.broadcastCandle("1w", dto);
    }


    public void generate1MonthCandle() {
        // ⏱️ 이번 달 1일 00:00 (UTC 기준)
        ZonedDateTime thisMonthStart = ZonedDateTime.now(ZoneOffset.UTC)
            .withDayOfMonth(1)
            .withHour(0).withMinute(0).withSecond(0).withNano(0);

        ZonedDateTime lastMonthStart = thisMonthStart.minusMonths(1);
        ZonedDateTime lastMonthEnd = thisMonthStart;

        // 🔍 지난달 1일부터 이번달 1일 전까지 일봉 조회
        List<BtcCandle1D> candles = btcCandle1DRepository.findCandlesBetween(
            lastMonthStart.toLocalDateTime(),
            lastMonthEnd.toLocalDateTime()
        );

        if (candles == null || candles.isEmpty()) return;

        // 📊 시가/고가/저가/종가/거래량 계산
        BigDecimal open = candles.get(0).getOpen();
        BigDecimal close = candles.get(candles.size() - 1).getClose();
        BigDecimal high = candles.stream().map(BtcCandle1D::getHigh).max(BigDecimal::compareTo).orElse(open);
        BigDecimal low  = candles.stream().map(BtcCandle1D::getLow).min(BigDecimal::compareTo).orElse(open);
        BigDecimal volume = candles.stream()
            .map(BtcCandle1D::getVolume)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 🧾 DB 저장
        BtcCandle1M candle = new BtcCandle1M();
        candle.setOpen(open);
        candle.setHigh(high);
        candle.setLow(low);
        candle.setClose(close);
        candle.setVolume(volume);
        candle.setCandleTime(lastMonthStart.toLocalDateTime());

        btcCandle1MRepository.insertCandle(candle);

        // 📤 WebSocket 전송 (선택 사항)
        // CandleDTO dto = CandleDTO.fromZonedUTC(lastMonthStart, open, high, low, close);
        // candleBroadcaster.broadcastCandle("1M", dto);
    }


}
