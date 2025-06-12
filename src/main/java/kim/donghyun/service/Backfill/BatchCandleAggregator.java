package kim.donghyun.service.Backfill;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.stereotype.Service;

import kim.donghyun.model.entity.*;
import kim.donghyun.repository.*;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BatchCandleAggregator {
    private final BtcCandle1MinRepository minRepo;
    private final BtcCandle15MinRepository min15Repo;
    private final BtcCandle1HRepository hRepo;
    private final BtcCandle1DRepository dRepo;
    private final BtcCandle1WRepository wRepo;
    private final BtcCandle1MRepository mRepo;

    /** 전체 기간에 대해 순차적으로 집계 */
    public void aggregateAll() {
        aggregate15Min();
        aggregate1Hour();
        aggregate1Day();
        aggregate1Week();
        aggregate1Month();
    }

    public void aggregate15Min() {
        LocalDateTime start = minRepo.findEarliestCandleTime();
        LocalDateTime end = minRepo.findLatestCandleTime();
        if (start == null || end == null) return;

        start = start.withSecond(0).withNano(0);
        start = start.minusMinutes(start.getMinute() % 15);
        for (LocalDateTime cursor = start; !cursor.isAfter(end); cursor = cursor.plusMinutes(15)) {
            LocalDateTime next = cursor.plusMinutes(15);
            List<BtcCandle1Min> candles = minRepo.findByTimeRange(cursor, next);
            if (candles == null || candles.isEmpty()) continue;

            BigDecimal open = candles.get(0).getOpen();
            BigDecimal close = candles.get(candles.size() - 1).getClose();
            BigDecimal high = candles.stream().map(BtcCandle1Min::getHigh).max(BigDecimal::compareTo).orElse(open);
            BigDecimal low  = candles.stream().map(BtcCandle1Min::getLow).min(BigDecimal::compareTo).orElse(open);
            BigDecimal volume = BigDecimal.ZERO;

            BtcCandle15Min c = new BtcCandle15Min();
            c.setOpen(open);
            c.setHigh(high);
            c.setLow(low);
            c.setClose(close);
            c.setVolume(volume);
            c.setCandleTime(cursor);
            min15Repo.insertCandle(c);
        }
    }

    public void aggregate1Hour() {
        LocalDateTime start = minRepo.findEarliestCandleTime();
        LocalDateTime end = minRepo.findLatestCandleTime();
        if (start == null || end == null) return;

        start = start.withMinute(0).withSecond(0).withNano(0);
        for (LocalDateTime cursor = start; !cursor.isAfter(end); cursor = cursor.plusHours(1)) {
            LocalDateTime next = cursor.plusHours(1);
            List<BtcCandle1Min> candles = minRepo.findByTimeRange(cursor, next);
            if (candles == null || candles.isEmpty()) continue;

            BigDecimal open = candles.get(0).getOpen();
            BigDecimal close = candles.get(candles.size() - 1).getClose();
            BigDecimal high = candles.stream().map(BtcCandle1Min::getHigh).max(BigDecimal::compareTo).orElse(open);
            BigDecimal low  = candles.stream().map(BtcCandle1Min::getLow).min(BigDecimal::compareTo).orElse(open);
            BigDecimal volume = BigDecimal.ZERO;

            BtcCandle1H c = new BtcCandle1H();
            c.setOpen(open);
            c.setHigh(high);
            c.setLow(low);
            c.setClose(close);
            c.setVolume(volume);
            c.setCandleTime(cursor);
            hRepo.insertCandle(c);
        }
    }

    public void aggregate1Day() {
        LocalDateTime start = minRepo.findEarliestCandleTime();
        LocalDateTime end = minRepo.findLatestCandleTime();
        if (start == null || end == null) return;

        start = start.toLocalDate().atStartOfDay();
        for (LocalDateTime cursor = start; !cursor.isAfter(end); cursor = cursor.plusDays(1)) {
            LocalDateTime next = cursor.plusDays(1);
            List<BtcCandle1H> candles = hRepo.findByTimeRange(cursor, next);
            if (candles == null || candles.isEmpty()) continue;

            BigDecimal open = candles.get(0).getOpen();
            BigDecimal close = candles.get(candles.size() - 1).getClose();
            BigDecimal high = candles.stream().map(BtcCandle1H::getHigh).max(BigDecimal::compareTo).orElse(open);
            BigDecimal low  = candles.stream().map(BtcCandle1H::getLow).min(BigDecimal::compareTo).orElse(open);
            BigDecimal volume = candles.stream().map(BtcCandle1H::getVolume).reduce(BigDecimal.ZERO, BigDecimal::add);

            BtcCandle1D c = new BtcCandle1D();
            c.setOpen(open);
            c.setHigh(high);
            c.setLow(low);
            c.setClose(close);
            c.setVolume(volume);
            c.setCandleTime(cursor);
            dRepo.insertCandle(c);
        }
    }

    public void aggregate1Week() {
        LocalDateTime start = minRepo.findEarliestCandleTime();
        LocalDateTime end = minRepo.findLatestCandleTime();
        if (start == null || end == null) return;

        start = start.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
        for (LocalDateTime cursor = start; !cursor.isAfter(end); cursor = cursor.plusWeeks(1)) {
            LocalDateTime next = cursor.plusWeeks(1);
            List<BtcCandle1D> candles = dRepo.findCandlesBetween(cursor, next);
            if (candles == null || candles.isEmpty()) continue;

            BigDecimal open = candles.get(0).getOpen();
            BigDecimal close = candles.get(candles.size() - 1).getClose();
            BigDecimal high = candles.stream().map(BtcCandle1D::getHigh).max(BigDecimal::compareTo).orElse(open);
            BigDecimal low  = candles.stream().map(BtcCandle1D::getLow).min(BigDecimal::compareTo).orElse(open);
            BigDecimal volume = candles.stream().map(BtcCandle1D::getVolume).reduce(BigDecimal.ZERO, BigDecimal::add);

            BtcCandle1W c = new BtcCandle1W();
            c.setOpen(open);
            c.setHigh(high);
            c.setLow(low);
            c.setClose(close);
            c.setVolume(volume);
            c.setCandleTime(cursor);
            wRepo.insertCandle(c);
        }
    }

    public void aggregate1Month() {
        LocalDateTime start = minRepo.findEarliestCandleTime();
        LocalDateTime end = minRepo.findLatestCandleTime();
        if (start == null || end == null) return;

        start = start.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atStartOfDay();
        for (LocalDateTime cursor = start; !cursor.isAfter(end); cursor = cursor.plusMonths(1)) {
            LocalDateTime next = cursor.plusMonths(1);
            List<BtcCandle1D> candles = dRepo.findCandlesBetween(cursor, next);
            if (candles == null || candles.isEmpty()) continue;

            BigDecimal open = candles.get(0).getOpen();
            BigDecimal close = candles.get(candles.size() - 1).getClose();
            BigDecimal high = candles.stream().map(BtcCandle1D::getHigh).max(BigDecimal::compareTo).orElse(open);
            BigDecimal low  = candles.stream().map(BtcCandle1D::getLow).min(BigDecimal::compareTo).orElse(open);
            BigDecimal volume = candles.stream().map(BtcCandle1D::getVolume).reduce(BigDecimal.ZERO, BigDecimal::add);

            BtcCandle1M c = new BtcCandle1M();
            c.setOpen(open);
            c.setHigh(high);
            c.setLow(low);
            c.setClose(close);
            c.setVolume(volume);
            c.setCandleTime(cursor);
            mRepo.insertCandle(c);
        }
    }
}