package kim.donghyun.model.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DepositRequest {
    private Long userId;
    private BigDecimal amount;
}