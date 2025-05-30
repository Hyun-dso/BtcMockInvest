package kim.donghyun.model.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class BtcPrice {
    private Long id;
    private BigDecimal price;
    private Date createdAt;
}
