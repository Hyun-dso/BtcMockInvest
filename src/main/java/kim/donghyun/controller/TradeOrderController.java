package kim.donghyun.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kim.donghyun.model.entity.TradeOrder;
import kim.donghyun.model.entity.User;
import kim.donghyun.model.enums.OrderMode;
import kim.donghyun.model.enums.OrderType;
import kim.donghyun.service.OrderService;
import kim.donghyun.service.TradeOrderHistoryService;
import kim.donghyun.util.PendingOrderCache;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class TradeOrderController {

    private final OrderService orderService;
    private final TradeOrderHistoryService orderHistoryService;
    private final PendingOrderCache pendingOrderCache;

    @PostMapping("/market")
    public ResponseEntity<TradeOrder> executeMarketOrder(
            @RequestParam("userId") Long userId,
            @RequestParam("type") String type,       // BUY or SELL
            @RequestParam("amount") BigDecimal amount,
            @RequestParam(value = "depth", defaultValue = "6") int depth) {

        TradeOrder order = orderService.executeMarketOrder(
            userId,
            OrderType.valueOf(type.toUpperCase()),  // 문자열을 enum으로 변환
            amount,
            depth
        );
        return ResponseEntity.ok(order);
    }

    @PostMapping("/execute")
    public ResponseEntity<TradeOrder> executeOrder(
            @RequestParam("userId") Long userId,
            @RequestParam("type") String type,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam(value = "price", required = false) BigDecimal price,
            @RequestParam("mode") String mode) {

        TradeOrder order = orderService.executeOrder(
            userId,
            OrderType.valueOf(type.toUpperCase()),
            amount,
            price,
            OrderMode.valueOf(mode.toUpperCase())
        );
        return ResponseEntity.ok(order);
    }
    

    @GetMapping("/pending")
    public ResponseEntity<java.util.List<TradeOrder>> getPendingOrders(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(orderService.getPendingOrders(loginUser.getId()));
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelOrder(@RequestParam("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/recent")
    public ResponseEntity<java.util.List<TradeOrder>> getRecentPendingOrders() {
        return ResponseEntity.ok(pendingOrderCache.getRecentOrders());
    }
    
    @GetMapping("/history")
    public ResponseEntity<java.util.List<TradeOrder>> getOrderHistory(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "limit", required = false, defaultValue = "30") int limit) {
        return ResponseEntity.ok(orderHistoryService.getHistory(userId, limit));
    }
}
