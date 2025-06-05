package kim.donghyun.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kim.donghyun.model.dto.PriceQuantityDTO;
import kim.donghyun.model.entity.TradeOrder;

public interface TradeOrderRepository {

    void insert(TradeOrder order);

    TradeOrder findById(Long orderId);

    List<TradeOrder> findByUserId(Long userId);

    void updateStatus(TradeOrder order);
    
    List<PriceQuantityDTO> findPendingLimitBids(@Param("depth") int depth);
    List<PriceQuantityDTO> findPendingLimitAsks(@Param("depth") int depth);
}
