package kim.donghyun.repository;

import kim.donghyun.model.entity.BtcCandle1W;
import java.util.List;

public interface BtcCandle1WRepository {
    void insertCandle(BtcCandle1W candle);
    BtcCandle1W findByCandleTime(String candleTime);
    List<BtcCandle1W> findRecentCandles(int limit);
}
