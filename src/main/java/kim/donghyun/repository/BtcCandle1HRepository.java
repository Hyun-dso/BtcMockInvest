package kim.donghyun.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import kim.donghyun.model.entity.BtcCandle1H;

public interface BtcCandle1HRepository {
    void insertCandle(BtcCandle1H candle);
    BtcCandle1H findByCandleTime(LocalDateTime candleTime);
    List<BtcCandle1H> findRecentCandles(int limit);
    List<BtcCandle1H> findByTimeRange(@Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
    List<BtcCandle1H> findBefore(@Param("end") LocalDateTime end, @Param("limit") int limit);
}
