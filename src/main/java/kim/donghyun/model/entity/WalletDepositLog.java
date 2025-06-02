package kim.donghyun.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WalletDepositLog {
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private BigDecimal beforeBalance;
    private BigDecimal afterBalance;
    private LocalDateTime createdAt;
}

