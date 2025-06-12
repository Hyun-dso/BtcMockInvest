package kim.donghyun.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import kim.donghyun.service.Backfill.BinanceBackfillService;

// 백필 러너 1분봉 데이터를 받아옴 (시간 설정은 BinanceBackfillService에서)
// 이후 Backfill/AggregationRunner로 1분봉을 기반으로 나머지 봉 집계
public class BackfillRunner {
    public static void main(String[] args) throws Exception {
        try (ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("backfill-context.xml")) {
            BinanceBackfillService service = ctx.getBean(BinanceBackfillService.class);
            service.backfill();
            
        }
    }
}