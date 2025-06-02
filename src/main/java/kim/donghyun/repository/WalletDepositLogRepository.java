package kim.donghyun.repository;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

public interface WalletDepositLogRepository {
    void insert(@Param("userId") Long userId,
                @Param("amount") BigDecimal amount,
                @Param("before") BigDecimal beforeBalance,
                @Param("after") BigDecimal afterBalance);
}
