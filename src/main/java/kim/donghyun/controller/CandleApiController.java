package kim.donghyun.controller;

import kim.donghyun.model.entity.BtcCandle1Min;
import kim.donghyun.repository.BtcCandle1MinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candle")
@RequiredArgsConstructor
public class CandleApiController {

    private final BtcCandle1MinRepository btcCandle1MinRepository;

    @GetMapping("/1min")
    public List<BtcCandle1Min> get1MinCandles(@RequestParam(defaultValue = "30") int limit) {
        return btcCandle1MinRepository.findRecentCandles(limit);
    }
}

// 1분 봉 차트 api
// GET http://localhost:8080/BtcMockInvest/api/candle/1min?limit=10
