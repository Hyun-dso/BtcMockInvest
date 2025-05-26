package kim.donghyun.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import kim.donghyun.model.entity.BtcPrice;
import kim.donghyun.repository.BtcPriceRepository;
import kim.donghyun.util.PriceCache;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BtcPriceService {

    private final BtcPriceRepository btcPriceRepository;
    private final PriceCache priceCache;

    public void savePrice(double price) {
        BtcPrice btcPrice = new BtcPrice();
        btcPrice.setPrice(BigDecimal.valueOf(price)); // ✅ 타입 맞춤	
        btcPriceRepository.insertPrice(btcPrice); // DB 저장
        priceCache.setLatestPrice(price);         // 캐싱
    }
}
