package kim.donghyun.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Wallet {
    private Long walletId;
    private Long userId;
    private BigDecimal btcBalance;
    private BigDecimal usdtBalance;
    private LocalDateTime createdAt;
}
