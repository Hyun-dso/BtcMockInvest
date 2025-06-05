package kim.donghyun.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import kim.donghyun.service.Backfill.BinanceBackfillService;

public class BackfillRunner {
    public static void main(String[] args) throws Exception {
        try (ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("backfill-context.xml")) {
            BinanceBackfillService service = ctx.getBean(BinanceBackfillService.class);
            service.backfill();
        }
    }
}