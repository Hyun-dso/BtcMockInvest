package kim.donghyun.repository;

import java.math.BigDecimal;
import org.apache.ibatis.annotations.Param;

public interface WalletResetLogRepository {
    void insertLog(
        @Param("userId") Long userId,
        @Param("beforeBtc") BigDecimal beforeBtc,
        @Param("beforeUsdt") BigDecimal beforeUsdt
    );
}