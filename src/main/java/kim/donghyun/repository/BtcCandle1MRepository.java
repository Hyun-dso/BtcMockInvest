package kim.donghyun.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import kim.donghyun.model.entity.BtcCandle1M;

public interface BtcCandle1MRepository {
    void insertCandle(BtcCandle1M candle);
    BtcCandle1M findByCandleTime(LocalDateTime candleTime);
    List<BtcCandle1M> findRecentCandles(int limit);
    List<BtcCandle1M> findBefore(@Param("end") LocalDateTime end, @Param("limit") int limit);
}
