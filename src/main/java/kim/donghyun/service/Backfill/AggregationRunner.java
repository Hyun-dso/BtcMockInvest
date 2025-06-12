package kim.donghyun.service.Backfill;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AggregationRunner {
    public static void main(String[] args) throws Exception {
        try (ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("backfill-context.xml")) {
            BatchCandleAggregator agg = ctx.getBean(BatchCandleAggregator.class);
            agg.aggregateAll();
        }
    }
}