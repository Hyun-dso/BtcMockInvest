package kim.donghyun.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
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
        
        // ✅ null 방어 필수!
        if (time == null || open == null || high == null || low == null || close == null) {
            System.out.println("❌ CandleDTO 변환 중 null 필드 존재. 변환 제외됨.");
            return null;
        }


        return CandleDTO.fromUTC(time, open, high, low, close);
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
                .filter(Objects::nonNull) // ✅ null 제거
                .collect(Collectors.toList());
    }
    
    public List<CandleDTO> get15MinCandleDTO(int limit) {
        return btcCandle15MinRepository.findRecentCandles(limit)
                .stream()
                .map(this::mapToDTO)
                .filter(Objects::nonNull) // ✅ null 제거
                .collect(Collectors.toList());
    }
    
    public List<CandleDTO> get1HCandleDTO(int limit) {
        return btcCandle1HRepository.findRecentCandles(limit)
                .stream()
                .map(this::mapToDTO)
                .filter(Objects::nonNull) // ✅ null 제거
                .collect(Collectors.toList());
    }
    
    public List<CandleDTO> get1DCandleDTO(int limit) {
        List<CandleDTO> raw = btcCandle1DRepository.findRecentCandles(limit)
            .stream()
            .map(this::mapToDTO)
            .filter(Objects::nonNull) // ✅ null 제거
            .sorted(java.util.Comparator.comparingLong(CandleDTO::getTime)) // 시간 오름차순 정렬
            .collect(Collectors.toList());

        return fillMissingCandles(raw, 86400); // 1일
    }
    
    public List<CandleDTO> get1WCandleDTO(int limit) {
        List<CandleDTO> raw = btcCandle1WRepository.findRecentCandles(limit)
            .stream()
            .map(this::mapToDTO)
            .filter(Objects::nonNull) // ✅ null 제거
            .sorted(java.util.Comparator.comparingLong(CandleDTO::getTime)) // 시간 오름차순 정렬
            .collect(Collectors.toList());

        return fillMissingCandles(raw, 604800); // 7일
    }
    
    
    
    public List<CandleDTO> get1MCandleDTO(int limit) {
        List<CandleDTO> raw = btcCandle1MRepository.findRecentCandles(limit)
            .stream()
            .map(this::mapToDTO)
            .filter(Objects::nonNull) // ✅ null 제거
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

            // 누락된 구간 보간 처리
            while (expectedTime < current.getTime()) {

                if (prev == null || prev.getClose() == null) {
                    System.out.println("❌ 보간 불가: close 값이 null, 시간: " + (prev != null ? prev.getTime() : "null"));
                    expectedTime += intervalSeconds;
                    continue; // skip 보간
                }

                CandleDTO interpolated = CandleDTO.fromUTC(
                	    LocalDateTime.ofEpochSecond(expectedTime, 0, ZoneOffset.UTC), // ✅ UTC 기준 시간
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
    
 // ✅ 실시간 임시 15분 캔들 생성
    public CandleDTO generateTemp15MinCandle() {
        // UTC 기준으로 현재 시각 계산
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC).withSecond(0).withNano(0);
        LocalDateTime start = now.minusMinutes(15);

        System.out.println("🧪 임시 15분봉 생성 시도: " + start + " ~ " + now);

        List<BtcCandle1Min> candles = btcCandle1MinRepository.findByTimeRange(start, now);
        System.out.println("🧪 포함된 1분봉 개수: " + (candles != null ? candles.size() : "null"));

        if (candles == null || candles.isEmpty()) return null;

        // ✅ DB에 저장된 가장 최근 15분봉과 시간 차이 확인
        List<CandleDTO> latest = get15MinCandleDTO(1); // 최근 1개
        if (!latest.isEmpty()) {
            long latestDbTime = latest.get(0).getTime(); // 초 단위
            long tempCandleTime = start.toEpochSecond(ZoneOffset.UTC);
            long diff = Math.abs(tempCandleTime - latestDbTime);

            if (diff > 3600) { // 1시간 이상 차이나면 생성하지 않음
                System.out.println("⚠️ DB 마지막 봉과 시간 차이 너무 큼 (" + diff + "초) → 임시 캔들 생략");
                return null;
            }
        }

        BigDecimal open = candles.get(0).getOpen();
        BigDecimal high = candles.stream().map(BtcCandle1Min::getHigh).max(BigDecimal::compareTo).orElse(open);
        BigDecimal low  = candles.stream().map(BtcCandle1Min::getLow).min(BigDecimal::compareTo).orElse(open);
        BigDecimal close = candles.get(candles.size() - 1).getClose();

        LocalDateTime candleTime = start;

        CandleDTO dto = CandleDTO.fromUTC(
        	    candleTime,
        	    open, high, low, close
        	);

        System.out.println("✅ 임시 15분봉 생성됨: " + dto);
        return dto;
    }
    
}


