package kim.donghyun.util;

import org.springframework.stereotype.Component;

@Component
public class PriceCache {

    // 명시적 초기화 (기본값은 0.0이지만 의도를 명확히 표시)
    private volatile double latestPrice = 0.0;

    public void setLatestPrice(double price) {
        this.latestPrice = price;
//        System.out.println("🧠 [SET] 캐시에 저장: " + price);
//        System.out.println("✅ set 호출된 인스턴스: " + System.identityHashCode(this));
    }

    public double getLatestPrice() {
//        System.out.println("🧠 [GET] 캐시에서 조회: " + latestPrice);
//        System.out.println("✅ get 호출된 인스턴스: " + System.identityHashCode(this));
        return latestPrice;
    }
}
