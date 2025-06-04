package kim.donghyun.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kim.donghyun.model.dto.CandleDTO;
import kim.donghyun.service.CandleService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/candle")
public class CandleApiController {

	private final CandleService candleService;

	@GetMapping
	public List<CandleDTO> getCandles(@RequestParam(name = "interval") String interval,
		    @RequestParam(name = "limit", defaultValue = "30") int limit) {

		System.out.println("📥 interval: " + interval + ", limit: " + limit);

		return switch (interval) {
		case "1m" -> candleService.get1MinCandleDTO(limit);
		case "15m" -> {
		    List<CandleDTO> candles = candleService.get15MinCandleDTO(limit);

		    CandleDTO tempCandle = candleService.generateTemp15MinCandle();
		    if (tempCandle != null) {
		        candles.add(tempCandle); // 마지막에 임시 캔들 추가
		    }

		    yield candles;
		}
		case "1h" -> candleService.get1HCandleDTO(limit);
		case "1d" -> candleService.get1DCandleDTO(limit);
		case "1w" -> candleService.get1WCandleDTO(limit);
		case "1M" -> candleService.get1MCandleDTO(limit);
		default -> throw new IllegalArgumentException("❌ 지원하지 않는 interval입니다: " + interval);
		};
	}
}

// 1분 봉 차트 api
// GET http://localhost:8080/BtcMockInvest/api/candle/1min?limit=10
