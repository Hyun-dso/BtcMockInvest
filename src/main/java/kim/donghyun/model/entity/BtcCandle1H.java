package kim.donghyun.model.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BtcCandle1H {
    private Long id;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private BigDecimal volume;
    private String candleTime;
}
