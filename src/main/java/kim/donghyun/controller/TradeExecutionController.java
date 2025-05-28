package kim.donghyun.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kim.donghyun.model.entity.TradeExecution;
import kim.donghyun.repository.TradeExecutionRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
public class TradeExecutionController {

    private final TradeExecutionRepository tradeExecutionRepository;

    @GetMapping("/history")
    public ResponseEntity<List<TradeExecution>> getTradeHistory(@RequestParam Long userId) {
        List<TradeExecution> history = tradeExecutionRepository.findByUserId(userId);
        return ResponseEntity.ok(history);
    }
}


// 체결 내역 조회 API
// GET /api/trade/history?userId=1