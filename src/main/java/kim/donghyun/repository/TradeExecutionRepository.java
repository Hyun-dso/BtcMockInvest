package kim.donghyun.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import kim.donghyun.model.entity.TradeExecution;

public interface TradeExecutionRepository {
    void insert(TradeExecution execution);
    List<TradeExecution> findAll();
    List<TradeExecution> findByUserId(Long userId); // 추적용 (JOIN 필요)
    BigDecimal findVolumeSince ( @Param("since") LocalDateTime since) ;
}
