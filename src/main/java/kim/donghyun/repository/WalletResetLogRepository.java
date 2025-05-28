package kim.donghyun.repository;

import java.math.BigDecimal;

public interface WalletResetLogRepository {
    void insertLog(Long userId, BigDecimal beforeBtc, BigDecimal beforeUsdt);
}
