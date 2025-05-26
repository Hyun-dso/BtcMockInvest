package kim.donghyun.repository;

import kim.donghyun.model.entity.BtcCandle1D;
import java.util.List;

public interface BtcCandle1DRepository {
    void insertCandle(BtcCandle1D candle);
    BtcCandle1D findByCandleTime(String candleTime);
    List<BtcCandle1D> findRecentCandles(int limit);
}
