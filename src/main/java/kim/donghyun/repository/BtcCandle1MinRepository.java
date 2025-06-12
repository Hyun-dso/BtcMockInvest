package kim.donghyun.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import kim.donghyun.model.entity.BtcCandle1Min;

public interface BtcCandle1MinRepository {
    void insertCandle(BtcCandle1Min candle);
    BtcCandle1Min findByCandleTime(LocalDateTime candleTime);
    List<BtcCandle1Min> findRecentCandles(int limit);
    List<BtcCandle1Min> findByTimeRange(@Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
    
    /** 테이블 내 가장 오래된 캔들 시각 */
    LocalDateTime findEarliestCandleTime();

    /** 테이블 내 가장 최근 캔들 시각 */
    LocalDateTime findLatestCandleTime();
}
