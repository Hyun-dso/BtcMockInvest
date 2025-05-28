package kim.donghyun.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import kim.donghyun.model.entity.BtcPrice;

public interface BtcPriceRepository {
    void insertPrice(BtcPrice price);
    BtcPrice findLatest();
    List<BtcPrice> findRecentPrices(int limit); // (optional)
    List<BtcPrice> findPricesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    

}
