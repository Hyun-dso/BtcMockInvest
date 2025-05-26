package kim.donghyun.repository;

import kim.donghyun.model.entity.BtcCandle15Min;
import java.util.List;

public interface BtcCandle15MinRepository {
    void insertCandle(BtcCandle15Min candle);
    BtcCandle15Min findByCandleTime(String candleTime);
    List<BtcCandle15Min> findRecentCandles(int limit);
}
