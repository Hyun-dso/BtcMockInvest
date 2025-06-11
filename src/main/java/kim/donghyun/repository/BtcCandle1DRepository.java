package kim.donghyun.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import kim.donghyun.model.entity.BtcCandle1D;

public interface BtcCandle1DRepository {
    void insertCandle(BtcCandle1D candle);
    BtcCandle1D findByCandleTime(LocalDateTime candleTime);
    List<BtcCandle1D> findRecentCandles(int limit);
    List<BtcCandle1D> findCandlesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    List<BtcCandle1D> findBefore(@Param("end") LocalDateTime end, @Param("limit") int limit);
}
