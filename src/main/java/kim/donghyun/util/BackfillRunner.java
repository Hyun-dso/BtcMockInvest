package kim.donghyun.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import kim.donghyun.service.Backfill.BatchCandleAggregator;
import kim.donghyun.service.Backfill.BinanceBackfillService;

public class BackfillRunner {
    public static void main(String[] args) throws Exception {
        try (ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("backfill-context.xml")) {
            BinanceBackfillService service = ctx.getBean(BinanceBackfillService.class);
            service.backfill();
            
            BatchCandleAggregator aggregator = ctx.getBean(BatchCandleAggregator.class);
            aggregator.generate15MinCandle();
            aggregator.generate1HourCandle();
            aggregator.generate1DayCandle();
            aggregator.generate1WeekCandle();
            aggregator.generate1MonthCandle();
        }
    }
}