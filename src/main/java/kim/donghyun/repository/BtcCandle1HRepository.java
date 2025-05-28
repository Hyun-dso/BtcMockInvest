package kim.donghyun.repository;

import java.time.LocalDateTime;
import java.util.List;

import kim.donghyun.model.entity.BtcCandle1H;

public interface BtcCandle1HRepository {
    void insertCandle(BtcCandle1H candle);
    BtcCandle1H findByCandleTime(LocalDateTime candleTime);
    List<BtcCandle1H> findRecentCandles(int limit);
}
