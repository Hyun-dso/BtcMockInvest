package kim.donghyun.repository;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

public interface WalletDepositLogRepository {
    void insert(@Param("userId") Long userId,
                @Param("amount") BigDecimal amount,
                @Param("before") BigDecimal beforeBalance,
                @Param("after") BigDecimal afterBalance);
    
    /** 최초 충전 금액 조회 */
    BigDecimal findFirstAmountByUserId(@Param("userId") Long userId);

    /** 마지막 초기화 이후 첫 충전 금액 조회 */
    BigDecimal findFirstAmountAfter(@Param("userId") Long userId,
                                   @Param("after") java.time.LocalDateTime after);
}
