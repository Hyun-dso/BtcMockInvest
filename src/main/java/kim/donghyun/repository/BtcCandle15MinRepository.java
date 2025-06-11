package kim.donghyun.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import kim.donghyun.model.entity.BtcCandle15Min;

public interface BtcCandle15MinRepository {
    void insertCandle(BtcCandle15Min candle);
    BtcCandle15Min findByCandleTime(LocalDateTime candleTime);
    List<BtcCandle15Min> findRecentCandles(int limit);
    List<BtcCandle15Min> findBefore(@Param("end") LocalDateTime end, @Param("limit") int limit);
}
