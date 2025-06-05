package kim.donghyun.model.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PriceQuantityDTO {
    private BigDecimal price;
    private BigDecimal totalQuantity;
}
