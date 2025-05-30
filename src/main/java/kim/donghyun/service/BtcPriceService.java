package kim.donghyun.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import kim.donghyun.model.entity.BtcPrice;
import kim.donghyun.repository.BtcPriceRepository;
import kim.donghyun.util.PriceCache;
import kim.donghyun.websocket.PriceWebSocketSender;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BtcPriceService {

    private final BtcPriceRepository btcPriceRepository;
    private final PriceCache priceCache;
    private final PriceWebSocketSender webSocketSender;

    public void savePrice(double price) {
    	// DB 저장
        BtcPrice btcPrice = new BtcPrice();
        btcPrice.setPrice(BigDecimal.valueOf(price)); // ✅ 타입 맞춤	
        btcPriceRepository.insertPrice(btcPrice); // DB 저장
        
        priceCache.setLatestPrice(price); // 캐싱
        
        webSocketSender.broadcast(price); // 웹소켓으로 전송
    }
    
    public Map<String, Object> getPriceWithUtcClose() {
        double currentPrice = priceCache.getLatestPrice();

        Map<String, Object> close = btcPriceRepository.findUtcClosePrice();
        BigDecimal prevClose = (BigDecimal) close.get("price");
        String time = (String) close.get("time");

        Map<String, Object> payload = new HashMap<>();
        payload.put("price", currentPrice);
        payload.put("prevClose", prevClose);
        payload.put("prevCloseTime", time);
        return payload;
    }
}
