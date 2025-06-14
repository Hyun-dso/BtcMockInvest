package kim.donghyun.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kim.donghyun.model.entity.TradeExecution;
import kim.donghyun.service.TradeHistoryService;
import kim.donghyun.util.TradeHistoryCache;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
public class TradeExecutionController {

    private final TradeHistoryService tradeHistoryService;
    private final TradeHistoryCache tradeHistoryCache;

    @GetMapping("/history")
    public ResponseEntity<List<TradeExecution>> getTradeHistory(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "limit", required = false, defaultValue = "30") int limit) {
        List<TradeExecution> history = tradeHistoryService.getHistory(userId, limit);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<List<Map<String, Object>>> getRecentTrades() {
        return ResponseEntity.ok(tradeHistoryCache.getRecentTrades());
    }
}


// 체결 내역 조회 API
// GET /api/trade/history?userId=1