package kim.donghyun.model.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BtcPrice {
    private Long id;
    private BigDecimal price;
    private String createdAt;
}
