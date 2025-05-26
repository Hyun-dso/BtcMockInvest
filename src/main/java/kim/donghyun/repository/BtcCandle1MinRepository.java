package kim.donghyun.repository;

import kim.donghyun.model.entity.BtcCandle1Min;
import java.util.List;

public interface BtcCandle1MinRepository {
    void insertCandle(BtcCandle1Min candle);
    BtcCandle1Min findByCandleTime(String candleTime);
    List<BtcCandle1Min> findRecentCandles(int limit);
}
