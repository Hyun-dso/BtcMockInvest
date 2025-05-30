package kim.donghyun.util;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class PriceFetcher {

    private static final String SYMBOL = "BTCUSDT";

    public double fetchPrice() {
        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.binance.com")
                .path("/api/v3/ticker/price")
                .queryParam("symbol", SYMBOL)
                .build()
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> response = restTemplate.getForObject(url, Map.class);
        
//        System.out.println("🌐 호출 URL: " + url);
//        System.out.println("📦 API 응답: " + response);

        if (response != null && response.containsKey("price")) {
            return new BigDecimal(response.get("price")).doubleValue();
        } else {
            throw new RuntimeException("Binance API로부터 가격 정보를 받아오지 못했습니다.");
        }
    }
}
