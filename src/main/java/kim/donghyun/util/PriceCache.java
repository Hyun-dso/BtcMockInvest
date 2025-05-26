package kim.donghyun.util;

import org.springframework.stereotype.Component;

@Component
public class PriceCache {
    private volatile double latestPrice;

    public double getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(double price) {
        this.latestPrice = price;
    }
}
