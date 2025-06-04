package kim.donghyun.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import kim.donghyun.model.dto.CandleDTO;

@Component
@RequiredArgsConstructor
public class CandleBroadcaster {

 private final SimpMessagingTemplate messagingTemplate;

 /**
  * @param interval 예: "1m", "15m", "1h", "1d", "1w", "1M"
  * @param candle 전송할 캔들 DTO (open, high, low, close, time 등 포함)
  */
 public void broadcastCandle(String interval, CandleDTO candle) {
     messagingTemplate.convertAndSend("/topic/candle/" + interval, candle);
 }
}
