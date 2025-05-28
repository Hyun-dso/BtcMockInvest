package kim.donghyun.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kim.donghyun.model.entity.TradeOrder;
import kim.donghyun.model.enums.OrderType;
import kim.donghyun.service.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class TradeOrderController {

    private final OrderService orderService;

    @PostMapping("/market")
    public ResponseEntity<TradeOrder> executeMarketOrder(
            @RequestParam("userId") Long userId,
            @RequestParam("type") String type,       // BUY or SELL
            @RequestParam("amount") BigDecimal amount) {

        TradeOrder order = orderService.executeMarketOrder(
            userId,
            OrderType.valueOf(type.toUpperCase()),  // 문자열을 enum으로 변환
            amount
        );
        return ResponseEntity.ok(order);
    }
}
