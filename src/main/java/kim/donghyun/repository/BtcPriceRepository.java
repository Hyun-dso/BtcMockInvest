package kim.donghyun.repository;

import kim.donghyun.model.entity.BtcPrice;
import java.util.List;

public interface BtcPriceRepository {
    void insertPrice(BtcPrice price);
    BtcPrice findLatest();
    List<BtcPrice> findRecentPrices(int limit); // (optional)
}
