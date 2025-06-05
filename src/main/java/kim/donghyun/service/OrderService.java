package kim.donghyun.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

import kim.donghyun.model.entity.TradeOrder;
import kim.donghyun.model.entity.TradeExecution;
import kim.donghyun.model.enums.OrderMode;
import kim.donghyun.model.enums.OrderStatus;
import kim.donghyun.model.enums.OrderType;
import kim.donghyun.service.strategy.OrderExecutionStrategy;
import kim.donghyun.repository.TradeOrderRepository;
import kim.donghyun.repository.TradeExecutionRepository;
import kim.donghyun.util.PriceCache;
import kim.donghyun.service.OrderBookService;
import kim.donghyun.service.TradePushService;
import kim.donghyun.service.WalletService;

@Service
public class OrderService {

    private final PriceCache priceCache;
    private final OrderBookService orderBookService;
    private final TradeOrderRepository orderRepository;
    private final TradePushService tradePushService;
    private final TradeExecutionRepository tradeExecutionRepository;
    private final WalletService walletService;

    private final Map<OrderMode, OrderExecutionStrategy> strategyMap = new EnumMap<>(OrderMode.class);

    public OrderService(PriceCache priceCache,
                        OrderBookService orderBookService,
                        TradeOrderRepository orderRepository,
                        TradePushService tradePushService,
                        TradeExecutionRepository tradeExecutionRepository,
                        WalletService walletService,
                        java.util.List<OrderExecutionStrategy> strategies) {
        this.priceCache = priceCache;
        this.orderBookService = orderBookService;
        this.orderRepository = orderRepository;
        this.tradePushService = tradePushService;
        this.tradeExecutionRepository = tradeExecutionRepository;
        this.walletService = walletService;
        for (OrderExecutionStrategy s : strategies) {
            if (s instanceof kim.donghyun.service.strategy.MarketOrderProcessor) {
                strategyMap.put(OrderMode.MARKET, s);
            } else if (s instanceof kim.donghyun.service.strategy.LimitOrderProcessor) {
                strategyMap.put(OrderMode.LIMIT, s);
            }
        }
    }

    public TradeOrder executeMarketOrder(Long userId, OrderType type, BigDecimal amount, int depth) {
        BigDecimal price = BigDecimal.valueOf(priceCache.getLatestPrice());

        // 타입에 따라 실제 대기 주문의 가격을 받아옴
        if (type == OrderType.BUY) {
            price = orderBookService.getPendingAsks(depth)
                    .keySet().stream().findFirst().orElse(price); // 매도 대기 주문
        } else {
            price = orderBookService.getPendingBids(depth)
                    .keySet().stream().findFirst().orElse(price); // 매수 대기 주문
        }

        BigDecimal total = price.multiply(amount);

        // ✅ 0. 지갑 잔고 차감 / 반영
        boolean success = walletService.applyTrade(userId, price, amount, type.name());
        if (!success) {
            throw new RuntimeException("잔고 부족으로 주문 실패");
        }

        // 1. 주문 저장
        TradeOrder order = new TradeOrder();
        order.setUserId(userId);
        order.setType(type);
        order.setAmount(amount);
        order.setPrice(price);
        order.setTotal(total);
        order.setOrderMode(OrderMode.MARKET);
        order.setStatus(OrderStatus.FILLED);
        orderRepository.insert(order);

        // 2. 체결 이력 저장
        TradeExecution execution = new TradeExecution();
        if (type == OrderType.BUY) {
            execution.setBuyOrderId(order.getOrderId());
            execution.setSellOrderId(null); // 상대 주문 없음 (시뮬레이션)
        } else {
            execution.setBuyOrderId(null);
            execution.setSellOrderId(order.getOrderId());
        }
        execution.setPrice(price);
        execution.setAmount(amount);
        tradeExecutionRepository.insert(execution);

        // 3. 체결 메시지 전송
        tradePushService.broadcastTrade(order);

        return order;
    }

    public TradeOrder executeOrder(Long userId, OrderType type, BigDecimal amount, BigDecimal price,
                                   OrderMode mode) {
        OrderExecutionStrategy strategy = strategyMap.get(mode);
        if (strategy == null) {
            throw new IllegalArgumentException("지원하지 않는 주문 모드");
        }
        return strategy.execute(userId, type, amount, price);
    }
}
