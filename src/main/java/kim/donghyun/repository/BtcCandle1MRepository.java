package kim.donghyun.repository;

import kim.donghyun.model.entity.BtcCandle1M;
import java.util.List;

public interface BtcCandle1MRepository {
    void insertCandle(BtcCandle1M candle);
    BtcCandle1M findByCandleTime(String candleTime);
    List<BtcCandle1M> findRecentCandles(int limit);
}
