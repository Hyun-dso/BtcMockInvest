package kim.donghyun.util;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PriceFetcher {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String url = "https://api.binance.com/api/v3/ticker/price?symbol=BTCUSDT";

    public double fetchPrice() {
        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = new JSONObject(response);
        return Double.parseDouble(json.getString("price"));
    }
}
