package kim.donghyun.repository;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import kim.donghyun.model.dto.PriceQuantityDTO;
import kim.donghyun.model.entity.TradeOrder;

public interface TradeOrderRepository {

    void insert(TradeOrder order);

    TradeOrder findById(Long orderId);

    List<TradeOrder> findByUserId(Long userId);

    List<TradeOrder> findPendingByUserId(Long userId);
    
    void updateStatus(TradeOrder order);
    
    
    List<PriceQuantityDTO> findPendingLimitBids(@Param("depth") int depth);
    List<PriceQuantityDTO> findPendingLimitAsks(@Param("depth") int depth);
    
    BigDecimal findPendingBidQuantityAtPrice(@Param("price") BigDecimal price);
    BigDecimal findPendingAskQuantityAtPrice(@Param("price") BigDecimal price);
    
    
    List<TradeOrder> findPendingOrdersByPrice ( @Param("price") BigDecimal price ) ;

    List<PriceQuantityDTO> findPendingQuantityGroupedByPrice();
    
    BigDecimal findPendingBidQuantityInRange(@Param("low") BigDecimal low,
                                             @Param("high") BigDecimal high);
    BigDecimal findPendingAskQuantityInRange(@Param("low") BigDecimal low,
                                             @Param("high") BigDecimal high);
}
