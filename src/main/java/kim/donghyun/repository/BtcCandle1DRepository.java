package kim.donghyun.repository;

import java.time.LocalDateTime;
import java.util.List;

import kim.donghyun.model.entity.BtcCandle1D;

public interface BtcCandle1DRepository {
    void insertCandle(BtcCandle1D candle);
    BtcCandle1D findByCandleTime(LocalDateTime candleTime);
    List<BtcCandle1D> findRecentCandles(int limit);
}
