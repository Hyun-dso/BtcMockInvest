package kim.donghyun.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import kim.donghyun.model.entity.UserPosition;
import kim.donghyun.repository.UserPositionRepository;
import kim.donghyun.util.PriceCache;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final UserPositionRepository positionRepository;
    private final PriceCache priceCache;

    public UserPosition getSpotPosition(Long userId) {
        UserPosition position = positionRepository.findSpotPositionByUserId(userId);

        if (position == null) return null;

        BigDecimal currentPrice = BigDecimal.valueOf(priceCache.getLatestPrice());
        position.setCurrentPrice(currentPrice);

        // 수익률 계산
        BigDecimal pnl = currentPrice.subtract(position.getEntryPrice())
                                     .divide(position.getEntryPrice(), 4, BigDecimal.ROUND_HALF_UP)
                                     .multiply(BigDecimal.valueOf(100));

        position.setPnlPercent(pnl);
        return position;
    }
}
