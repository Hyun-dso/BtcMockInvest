package kim.donghyun.model.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Wallet {
    private Long walletId;
    private Long userId;
    private BigDecimal btcBalance;
    private BigDecimal usdtBalance;
    private BigDecimal initialValue;
    private BigDecimal currentPrice;  // 현재가 필드 추가
    private LocalDateTime createdAt;

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getBtcBalance() {
        return btcBalance == null ? BigDecimal.ZERO : btcBalance;
    }

    public BigDecimal getUsdtBalance() {
        return usdtBalance == null ? BigDecimal.ZERO : usdtBalance;
    }

    public BigDecimal getInitialValue() {
        return initialValue == null ? BigDecimal.ZERO : initialValue;
    }

    public BigDecimal getProfitRateValue() {
        if (getInitialValue().compareTo(BigDecimal.ZERO) == 0 || currentPrice == null) {
            return BigDecimal.ZERO;
        }
        try {
            BigDecimal totalValue = getUsdtBalance().add(getBtcBalance().multiply(currentPrice));
            BigDecimal rate = totalValue.subtract(getInitialValue())
                    .divide(getInitialValue(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            return rate.setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public String getProfitRateSafe() {
        return getProfitRateValue().toPlainString();
    }

    public String getTotalValue() {
        if (currentPrice == null) return "0.00";
        try {
            BigDecimal total = getUsdtBalance().add(getBtcBalance().multiply(currentPrice));
            return total.setScale(2, RoundingMode.HALF_UP).toPlainString();
        } catch (Exception e) {
            return "0.00";
        }
    }
}
