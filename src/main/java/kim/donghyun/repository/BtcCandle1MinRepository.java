package kim.donghyun.repository;

import java.time.LocalDateTime;
import java.util.List;

import kim.donghyun.model.entity.BtcCandle1Min;

public interface BtcCandle1MinRepository {
    void insertCandle(BtcCandle1Min candle);
    BtcCandle1Min findByCandleTime(LocalDateTime candleTime);
    List<BtcCandle1Min> findRecentCandles(int limit);
}
