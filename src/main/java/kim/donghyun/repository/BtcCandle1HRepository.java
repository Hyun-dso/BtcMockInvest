package kim.donghyun.repository;

import kim.donghyun.model.entity.BtcCandle1H;
import java.util.List;

public interface BtcCandle1HRepository {
    void insertCandle(BtcCandle1H candle);
    BtcCandle1H findByCandleTime(String candleTime);
    List<BtcCandle1H> findRecentCandles(int limit);
}
