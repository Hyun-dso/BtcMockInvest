package kim.donghyun.repository;

import java.time.LocalDateTime;
import java.util.List;

import kim.donghyun.model.entity.BtcCandle15Min;

public interface BtcCandle15MinRepository {
    void insertCandle(BtcCandle15Min candle);
    BtcCandle15Min findByCandleTime(LocalDateTime candleTime);
    List<BtcCandle15Min> findRecentCandles(int limit);
}
