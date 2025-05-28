package kim.donghyun.repository;

import kim.donghyun.model.entity.TradeExecution;
import java.util.List;

public interface TradeExecutionRepository {
    void insert(TradeExecution execution);
    List<TradeExecution> findAll();
    List<TradeExecution> findByUserId(Long userId); // 추적용 (JOIN 필요)
}
