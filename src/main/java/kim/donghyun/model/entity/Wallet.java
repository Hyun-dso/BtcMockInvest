package kim.donghyun.model.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Wallet {
    private Long walletId;
    private Long userId;
    private BigDecimal btcBalance;
    private BigDecimal usdtBalance;
    private String createdAt;
}
