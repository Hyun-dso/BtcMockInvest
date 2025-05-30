package kim.donghyun.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import kim.donghyun.model.entity.BtcPrice;

public interface BtcPriceRepository {
    void insertPrice(BtcPrice price);
    BtcPrice findLatest();
    BtcPrice findClosestAfter(LocalDateTime referenceTime);
    List<BtcPrice> findRecentPrices(int limit); // (optional)
    List<BtcPrice> findPricesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    Map<String, Object> findUtcClosePrice();

}
