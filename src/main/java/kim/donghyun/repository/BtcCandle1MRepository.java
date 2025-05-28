package kim.donghyun.repository;

import java.time.LocalDateTime;
import java.util.List;

import kim.donghyun.model.entity.BtcCandle1M;

public interface BtcCandle1MRepository {
    void insertCandle(BtcCandle1M candle);
    BtcCandle1M findByCandleTime(LocalDateTime candleTime);
    List<BtcCandle1M> findRecentCandles(int limit);
}
