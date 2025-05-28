package kim.donghyun.repository;

import java.time.LocalDateTime;
import java.util.List;

import kim.donghyun.model.entity.BtcCandle1W;

public interface BtcCandle1WRepository {
    void insertCandle(BtcCandle1W candle);
    BtcCandle1W findByCandleTime(LocalDateTime candleTime);
    List<BtcCandle1W> findRecentCandles(int limit);
}
