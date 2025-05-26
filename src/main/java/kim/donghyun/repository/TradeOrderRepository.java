package kim.donghyun.repository;

import kim.donghyun.model.entity.TradeOrder;
import java.util.List;

public interface TradeOrderRepository {

    void insert(TradeOrder order);

    TradeOrder findById(Long orderId);

    List<TradeOrder> findByUserId(Long userId);

    void updateStatus(TradeOrder order);
}
