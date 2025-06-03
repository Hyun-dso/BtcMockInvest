package kim.donghyun.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
import kim.donghyun.repository.BtcCandle15MinRepository;
import kim.donghyun.repository.BtcCandle1DRepository;
import kim.donghyun.repository.BtcCandle1HRepository;
import kim.donghyun.repository.BtcCandle1MRepository;
import kim.donghyun.repository.BtcCandle1MinRepository;
import kim.donghyun.repository.BtcCandle1WRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CandleService {

    private final BtcCandle1MinRepository btcCandle1MinRepository;
    private final BtcCandle15MinRepository btcCandle15MinRepository;
    private final BtcCandle1HRepository btcCandle1HRepository;
    private final BtcCandle1DRepository btcCandle1DRepository;
    private final BtcCandle1WRepository btcCandle1WRepository;
    private final BtcCandle1MRepository btcCandle1MRepository;

    // 🔽 이게 바로 내부에서 쓰이는 private 변환 메서드!
    private CandleDTO mapToDTO(Object candle) {
        LocalDateTime time;
        BigDecimal open, high, low, close;

        if (candle instanceof BtcCandle1Min c) {
            time = c.getCandleTime(); open = c.getOpen(); high = c.getHigh(); low = c.getLow(); close = c.getClose();
        } else if (candle instanceof BtcCandle15Min c) {
            time = c.getCandleTime(); open = c.getOpen(); high = c.getHigh(); low = c.getLow(); close = c.getClose();
        } else if (candle instanceof BtcCandle1H c) {
            time = c.getCandleTime(); open = c.getOpen(); high = c.getHigh(); low = c.getLow(); close = c.getClose();
        } else if (candle instanceof BtcCandle1D c) {
            time = c.getCandleTime(); open = c.getOpen(); high = c.getHigh(); low = c.getLow(); close = c.getClose();
        } else if (candle instanceof BtcCandle1W c) {
            time = c.getCandleTime(); open = c.getOpen(); high = c.getHigh(); low = c.getLow(); close = c.getClose();
        } else if (candle instanceof BtcCandle1M c) {
            time = c.getCandleTime(); open = c.getOpen(); high = c.getHigh(); low = c.getLow(); close = c.getClose();
        } else {
            throw new IllegalArgumentException("❌ Unknown candle type: " + candle.getClass());
        }

        return new CandleDTO(
            time.toEpochSecond(ZoneOffset.UTC),
            time.toString().replace("T", " ").substring(0, 16),
            open, high, low, close
        );
    }
    
    public List<BtcCandle1Min> get1MinCandles(int limit) {
        return btcCandle1MinRepository.findRecentCandles(limit);
    }

    public List<BtcCandle15Min> get15MinCandles(int limit) {
        return btcCandle15MinRepository.findRecentCandles(limit);
    }

    public List<BtcCandle1H> get1HourCandles(int limit) {
        return btcCandle1HRepository.findRecentCandles(limit);
    }

    public List<BtcCandle1D> get1DayCandles(int limit) {
        return btcCandle1DRepository.findRecentCandles(limit);
    }

    public List<BtcCandle1W> get1WeekCandles(int limit) {
        return btcCandle1WRepository.findRecentCandles(limit);
    }

    public List<BtcCandle1M> get1MonthCandles(int limit) {
        return btcCandle1MRepository.findRecentCandles(limit);
    }
    
    public List<CandleDTO> get1MinCandleDTO(int limit) {
        return btcCandle1MinRepository.findRecentCandles(limit)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public List<CandleDTO> get15MinCandleDTO(int limit) {
        return btcCandle15MinRepository.findRecentCandles(limit)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public List<CandleDTO> get1HCandleDTO(int limit) {
        return btcCandle1HRepository.findRecentCandles(limit)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public List<CandleDTO> get1DCandleDTO(int limit) {
        List<CandleDTO> raw = btcCandle1DRepository.findRecentCandles(limit)
            .stream()
            .map(this::mapToDTO)
            .sorted(java.util.Comparator.comparingLong(CandleDTO::getTime)) // 시간 오름차순 정렬
            .collect(Collectors.toList());

        return fillMissingCandles(raw, 86400); // 1일
    }
    
    public List<CandleDTO> get1WCandleDTO(int limit) {
        List<CandleDTO> raw = btcCandle1WRepository.findRecentCandles(limit)
            .stream()
            .map(this::mapToDTO)
            .sorted(java.util.Comparator.comparingLong(CandleDTO::getTime)) // 시간 오름차순 정렬
            .collect(Collectors.toList());

        return fillMissingCandles(raw, 604800); // 7일
    }
    
    
    
    public List<CandleDTO> get1MCandleDTO(int limit) {
        List<CandleDTO> raw = btcCandle1MRepository.findRecentCandles(limit)
            .stream()
            .map(this::mapToDTO)
            .sorted(java.util.Comparator.comparingLong(CandleDTO::getTime)) // 시간 오름차순 정렬
            .collect(Collectors.toList());

        return fillMissingCandles(raw, 2629743); // 1달
    }
    
    private List<CandleDTO> fillMissingCandles(List<CandleDTO> originalList, long intervalSeconds) {
        if (originalList == null || originalList.size() < 2) return originalList;

        List<CandleDTO> filled = new java.util.ArrayList<>();
        CandleDTO prev = originalList.get(0);
        filled.add(prev);

        for (int i = 1; i < originalList.size(); i++) {
            CandleDTO current = originalList.get(i);
            long expectedTime = prev.getTime() + intervalSeconds;

            // 누락된 구간 채움
            while (expectedTime < current.getTime()) {
                CandleDTO interpolated = new CandleDTO(
                    expectedTime,
                    formatTimeLabel(expectedTime),
                    prev.getClose(), // open
                    prev.getClose(), // high
                    prev.getClose(), // low
                    prev.getClose()  // close
                );
                filled.add(interpolated);
                expectedTime += intervalSeconds;
            }

            filled.add(current);
            prev = current;
        }

        return filled;
    }

    private String formatTimeLabel(long unixTime) {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(unixTime, 0, ZoneOffset.UTC);
        return dateTime.toString().replace("T", " ").substring(0, 16);
    }
}
