package kim.donghyun.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import kim.donghyun.model.entity.BtcCandle1W;

public interface BtcCandle1WRepository {
    void insertCandle(BtcCandle1W candle);
    BtcCandle1W findByCandleTime(LocalDateTime candleTime);
    List<BtcCandle1W> findRecentCandles(int limit);
    List<BtcCandle1W> findBefore(@Param("end") LocalDateTime end, @Param("limit") int limit);
}
